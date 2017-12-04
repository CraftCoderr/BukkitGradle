package ru.endlesscode.bukkitgradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.compile.JavaCompile
import ru.endlesscode.bukkitgradle.util.Dependencies

class BukkitGradlePlugin implements Plugin<Project> {
    final static String GROUP = 'Bukkit'

    Project project

    static boolean isTesting() {
        System.properties['test'] == 'true'
    }

    @Override
    void apply(Project project) {
        this.project = project
        configureProject()
    }

    /**
     * Configures project
     */
    void configureProject() {
        addPlugins()
        configureEncoding()
        addRepositories()
        addDependencies()
    }

    /**
     * Adds all needed plugins
     */
    void addPlugins() {
        project.with {
            plugins.with {
                apply('java')
                apply('eclipse')
                apply('idea')
                apply(PluginMetaPlugin)
                apply(DevServerPlugin)
            }

            convention.getPlugin(JavaPluginConvention).with {
                sourceCompatibility = targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }

    /**
     * Sets force encoding on compile to UTF-8
     */
    void configureEncoding() {
        project.tasks.withType(JavaCompile) {
            options.encoding = 'UTF-8'
        }
    }

    /**
     * Adds needed repositories
     */
    void addRepositories() {
        project.with {
            repositories {
                mavenLocal()
                mavenCentral()

                maven {
                    name = 'sk89q'
                    url = 'http://maven.sk89q.com/repo/'
                }

                maven {
                    name = 'spigot'
                    url = 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/'
                }
            }
        }
    }

    /**
     * Adds needed dependencies
     */
    void addDependencies() {
        Dependencies.configureProject(project)
    }
}
