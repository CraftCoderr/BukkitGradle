package ru.endlesscode.gradle.bukkit.meta

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Path

class GenerateMeta extends DefaultTask {
    @Input
    PluginMeta meta
    @Input
    Path target

    Path getTarget() {
        return this.target ?: temporaryDir.toPath().resolve(PluginMetaPlugin.META_FILE)
    }

    @TaskAction
    def generateMeta() {
        def metaItems = meta.items
        validateMeta(metaItems)

        List<String> lines = convertToYaml(metaItems)
        File file = PluginMetaPlugin.getMetaFile(project)
        if (file.exists()) {
            PluginMetaPlugin.removeAllMeta(file)
            lines.addAll(file.readLines())
        }

        getTarget().withWriter { Writer writer ->
            lines.each { String line ->
                writer.write("$line\n")
            }
        }
    }

    private static def validateMeta(List<MetaItem> metaItems) {
        for (MetaItem item in metaItems) {
            if (item.required && !metaItems.value) {
                throw new GradleException("Plugin metadata parse error: '$item.id' must not be null")
            }
        }
    }

    private static List<String> convertToYaml(List<MetaItem> metaItems) {
        List<String> yaml = []
        metaItems.each { MetaItem item ->
            if (item.value != null) {
                yaml.add(item.entry)
            }
        }

        return yaml
    }
}
