package com.stardashup.client.core.config;

import com.google.gson.*;
import com.stardashup.client.SDUClient;
import com.stardashup.client.core.log.SDULogger;
import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.setting.Setting;
import com.stardashup.client.module.hud.HUDModule;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages JSON-based configuration persistence.
 *
 * <p>Configuration files are stored in the {@code stardashup/config/} directory.
 * Supports named profiles for saving/loading complete configurations.</p>
 */
public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final File configDir;
    private final File profilesDir;
    private final File mainConfigFile;
    private String activeProfile;

    public ConfigManager(File configDir) {
        this.configDir = configDir;
        this.profilesDir = new File(configDir, "profiles");
        this.mainConfigFile = new File(configDir, "modules.json");
        this.activeProfile = "default";

        configDir.mkdirs();
        profilesDir.mkdirs();
    }

    // -------------------------------------------------------------------------
    // Save / Load All
    // -------------------------------------------------------------------------

    /**
     * Saves all module states, settings, and HUD positions.
     */
    public void saveAll() {
        try {
            JsonObject root = new JsonObject();

            for (Module module : SDUClient.getInstance().getModuleManager().getModules()) {
                JsonObject moduleObj = new JsonObject();
                moduleObj.addProperty("enabled", module.isEnabled());
                moduleObj.addProperty("keyBind", module.getKeyBind());

                // Settings
                JsonObject settingsObj = new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    settingsObj.addProperty(setting.getName(), setting.serialize());
                }
                moduleObj.add("settings", settingsObj);

                // HUD position data
                if (module instanceof HUDModule) {
                    HUDModule hud = (HUDModule) module;
                    JsonObject posObj = new JsonObject();
                    posObj.addProperty("x", hud.getX());
                    posObj.addProperty("y", hud.getY());
                    moduleObj.add("position", posObj);
                }

                root.add(module.getName(), moduleObj);
            }

            writeJsonFile(mainConfigFile, root);
            SDULogger.staticDebug("Configuration saved.");
        } catch (Exception e) {
            SDULogger.staticError("Failed to save configuration!", e);
        }
    }

    /**
     * Loads all module states, settings, and HUD positions.
     */
    public void loadAll() {
        if (!mainConfigFile.exists()) {
            SDULogger.staticInfo("No configuration file found. Using defaults.");
            return;
        }

        try {
            JsonObject root = readJsonFile(mainConfigFile);
            if (root == null) return;

            for (Module module : SDUClient.getInstance().getModuleManager().getModules()) {
                if (!root.has(module.getName())) continue;

                JsonObject moduleObj = root.getAsJsonObject(module.getName());

                // Enabled state
                if (moduleObj.has("enabled") && moduleObj.get("enabled").getAsBoolean()) {
                    module.setEnabled(true);
                }

                // Keybind
                if (moduleObj.has("keyBind")) {
                    module.setKeyBind(moduleObj.get("keyBind").getAsInt());
                }

                // Settings
                if (moduleObj.has("settings")) {
                    JsonObject settingsObj = moduleObj.getAsJsonObject("settings");
                    for (Setting<?> setting : module.getSettings()) {
                        if (settingsObj.has(setting.getName())) {
                            setting.deserialize(settingsObj.get(setting.getName()).getAsString());
                        }
                    }
                }

                // HUD position
                if (module instanceof HUDModule && moduleObj.has("position")) {
                    HUDModule hud = (HUDModule) module;
                    JsonObject posObj = moduleObj.getAsJsonObject("position");
                    if (posObj.has("x")) hud.setX(posObj.get("x").getAsInt());
                    if (posObj.has("y")) hud.setY(posObj.get("y").getAsInt());
                }
            }

            SDULogger.staticInfo("Configuration loaded.");
        } catch (Exception e) {
            SDULogger.staticError("Failed to load configuration!", e);
        }
    }

    // -------------------------------------------------------------------------
    // Profiles
    // -------------------------------------------------------------------------

    /**
     * Saves the current configuration as a named profile.
     */
    public void saveProfile(String profileName) {
        this.activeProfile = profileName;
        File profileFile = new File(profilesDir, profileName + ".json");
        // Re-use saveAll logic but to a different file
        try {
            JsonObject root = new JsonObject();
            for (Module module : SDUClient.getInstance().getModuleManager().getModules()) {
                JsonObject moduleObj = new JsonObject();
                moduleObj.addProperty("enabled", module.isEnabled());
                moduleObj.addProperty("keyBind", module.getKeyBind());

                JsonObject settingsObj = new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    settingsObj.addProperty(setting.getName(), setting.serialize());
                }
                moduleObj.add("settings", settingsObj);

                if (module instanceof HUDModule) {
                    HUDModule hud = (HUDModule) module;
                    JsonObject posObj = new JsonObject();
                    posObj.addProperty("x", hud.getX());
                    posObj.addProperty("y", hud.getY());
                    moduleObj.add("position", posObj);
                }
                root.add(module.getName(), moduleObj);
            }
            writeJsonFile(profileFile, root);
            SDULogger.staticInfo("Profile '" + profileName + "' saved.");
        } catch (Exception e) {
            SDULogger.staticError("Failed to save profile '" + profileName + "'!", e);
        }
    }

    /**
     * Loads a named profile.
     */
    public void loadProfile(String profileName) {
        File profileFile = new File(profilesDir, profileName + ".json");
        if (!profileFile.exists()) {
            SDULogger.staticWarn("Profile '" + profileName + "' not found.");
            return;
        }
        this.activeProfile = profileName;

        // Disable all modules first
        for (Module module : SDUClient.getInstance().getModuleManager().getModules()) {
            if (module.isEnabled()) module.setEnabled(false);
        }

        // Load from profile file (reuse the main load logic with different file)
        File backup = mainConfigFile;
        try {
            JsonObject root = readJsonFile(profileFile);
            if (root == null) return;

            for (Module module : SDUClient.getInstance().getModuleManager().getModules()) {
                if (!root.has(module.getName())) continue;

                JsonObject moduleObj = root.getAsJsonObject(module.getName());

                if (moduleObj.has("enabled") && moduleObj.get("enabled").getAsBoolean()) {
                    module.setEnabled(true);
                }
                if (moduleObj.has("keyBind")) {
                    module.setKeyBind(moduleObj.get("keyBind").getAsInt());
                }
                if (moduleObj.has("settings")) {
                    JsonObject settingsObj = moduleObj.getAsJsonObject("settings");
                    for (Setting<?> setting : module.getSettings()) {
                        if (settingsObj.has(setting.getName())) {
                            setting.deserialize(settingsObj.get(setting.getName()).getAsString());
                        }
                    }
                }
                if (module instanceof HUDModule && moduleObj.has("position")) {
                    HUDModule hud = (HUDModule) module;
                    JsonObject posObj = moduleObj.getAsJsonObject("position");
                    if (posObj.has("x")) hud.setX(posObj.get("x").getAsInt());
                    if (posObj.has("y")) hud.setY(posObj.get("y").getAsInt());
                }
            }
            SDULogger.staticInfo("Profile '" + profileName + "' loaded.");
        } catch (Exception e) {
            SDULogger.staticError("Failed to load profile '" + profileName + "'!", e);
        }
    }

    /**
     * Returns a list of available profile names.
     */
    public List<String> getProfileNames() {
        List<String> names = new ArrayList<String>();
        File[] files = profilesDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    names.add(file.getName().replace(".json", ""));
                }
            }
        }
        return names;
    }

    /**
     * Deletes a named profile.
     */
    public boolean deleteProfile(String profileName) {
        File profileFile = new File(profilesDir, profileName + ".json");
        return profileFile.exists() && profileFile.delete();
    }

    public String getActiveProfile() {
        return activeProfile;
    }

    public File getConfigDir() {
        return configDir;
    }

    // -------------------------------------------------------------------------
    // JSON I/O helpers
    // -------------------------------------------------------------------------

    private void writeJsonFile(File file, JsonObject json) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, UTF8);
            writer.write(GSON.toJson(json));
        } finally {
            if (writer != null) writer.close();
            if (fos != null) fos.close();
        }
    }

    private JsonObject readJsonFile(File file) {
        FileInputStream fis = null;
        InputStreamReader reader = null;
        try {
            fis = new FileInputStream(file);
            reader = new InputStreamReader(fis, UTF8);
            JsonElement element = new JsonParser().parse(reader);
            if (element.isJsonObject()) {
                return element.getAsJsonObject();
            }
        } catch (Exception e) {
            SDULogger.staticError("Failed to read JSON file: " + file.getName(), e);
        } finally {
            try {
                if (reader != null) reader.close();
                if (fis != null) fis.close();
            } catch (IOException ignored) {
            }
        }
        return null;
    }
}
