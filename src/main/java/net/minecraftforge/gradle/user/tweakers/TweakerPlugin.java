/*
 * A Gradle plugin for the creation of Minecraft mods and MinecraftForge plugins.
 * Copyright (C) 2013 Minecraft Forge
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package net.minecraftforge.gradle.user.tweakers;

import com.google.common.base.Strings;
import net.minecraftforge.gradle.common.Constants;
import net.minecraftforge.gradle.user.UserVanillaBasePlugin;
import org.gradle.api.tasks.bundling.Jar;

import java.util.List;

import static net.minecraftforge.gradle.common.Constants.REPLACE_CACHE_DIR;
import static net.minecraftforge.gradle.common.Constants.REPLACE_MC_VERSION;

public abstract class TweakerPlugin extends UserVanillaBasePlugin<TweakerExtension> {
    @Override
    protected void applyVanillaUserPlugin() {
        // add launchwrapper dep.. cuz everyone uses it apperantly..
        TweakerExtension extension = getExtension();
        if (extension.isLaunchwrapper()) {
            project.getDependencies().add(Constants.CONFIG_MC_DEPS, "net.minecraft:launchwrapper:1.12");
        }
    }

    @Override
    protected void afterEvaluate() {
        super.afterEvaluate();

        TweakerExtension ext = getExtension();


        if (!Strings.isNullOrEmpty(ext.getTweakClass())) {
            // add fml tweaker to manifest
            Jar jarTask = (Jar) project.getTasks().getByName("jar");
            jarTask.getManifest().getAttributes().put("TweakClass", ext.getTweakClass());
        }
    }

    @Override
    protected String getClientTweaker(TweakerExtension ext) {
        return "";// nothing, put it in as an argument
    }

    @Override
    protected String getServerTweaker(TweakerExtension ext) {
        return "";// nothing, put it in as an argument
    }

    @Override
    protected String getClientRunClass(TweakerExtension ext) {
        return ext.getMainClass();
    }

    @Override
    protected List<String> getClientRunArgs(TweakerExtension ext) {
        List<String> out = super.getClientRunArgs(ext);
        if (!Strings.isNullOrEmpty(ext.getTweakClass())) {
            out.add("--tweakClass");
            out.add(ext.getTweakClass());
        }
        return out;
    }

    @Override
    protected String getServerRunClass(TweakerExtension ext) {
        return ext.getMainClass();
    }

    @Override
    protected List<String> getServerRunArgs(TweakerExtension ext) {
        List<String> out = super.getServerRunArgs(ext);
        if (!Strings.isNullOrEmpty(ext.getTweakClass())) {
            out.add("--tweakClass");
            out.add(ext.getTweakClass());
        }
        return out;
    }

    @Override
    protected List<String> getClientJvmArgs(TweakerExtension ext) {
        return ext.getResolvedClientJvmArgs();
    }

    @Override
    protected List<String> getServerJvmArgs(TweakerExtension ext) {
        return ext.getResolvedServerJvmArgs();
    }

    @Override
    protected Object getStartDir() {
        return delayedFile(REPLACE_CACHE_DIR + "/net/minecraft/" + getJarName() + "/" + REPLACE_MC_VERSION + "/bookmc-start");
    }
}
