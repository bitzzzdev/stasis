package com.bitz.smartticks.config;

import me.shedaniel.autoconfig.AutoConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class SmartTicksModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(SmartTicksConfig.class, parent).get();
    }
}
