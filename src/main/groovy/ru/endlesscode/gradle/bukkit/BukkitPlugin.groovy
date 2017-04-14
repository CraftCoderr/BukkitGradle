package ru.endlesscode.gradle.bukkit

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention

class BukkitPlugin implements Plugin<Project> {
    Project project

    @Override
    void apply(Project project) {
        this.project = project
        configureProject()
    }

    def configureProject() {
        addPlugins()
        addRepositories()
        addDependencies()
    }

    def addPlugins() {
        project.with {
            plugins.with {
                apply('java')
                apply('eclipse')
                apply('idea')
            }

            convention.getPlugin(JavaPluginConvention).with {
                sourceCompatibility = targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }

    def addRepositories() {
        project.with {
            repositories {
                mavenLocal()
                mavenCentral()

                maven {
                    name = 'spigot'
                    url = 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/'
                }
            }
        }
    }

    def addDependencies() {
        project.with {
            extensions.create(BukkitPluginExtension.NAME, BukkitPluginExtension)

            dependencies {
                compile group: 'org.bukkit', name: 'bukkit', version: "$bukkit.version"
            }
        }
    }
}
