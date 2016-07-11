package org.m2ci.msp

import org.gradle.testkit.runner.GradleRunner
import org.testng.annotations.*

import static org.gradle.testkit.runner.TaskOutcome.*

class SoxWrapperPluginFunctionalTest {

    def gradle

    @BeforeClass
    void setUp() {
        gradle = GradleRunner.create().withPluginClasspath()
    }

    File initBuildScript() {
        def projectDir = File.createTempDir()
        def buildScriptFile = new File(projectDir, 'build.gradle')
        buildScriptFile.withWriter { buildScript ->
            buildScript << [
                    'plugins {',
                    '  id "org.m2ci.msp.sox-wrapper"',
                    '}',
                    ''
            ].join('\n')
        }
        buildScriptFile
    }

    @Test
    void canApplyPlugin() {
        def buildScript = initBuildScript()
        buildScript << [
                'task test << {',
                '  assert plugins.findPlugin("org.m2ci.msp.sox-wrapper")',
                '}',
                'defaultTasks "test"'
        ].join('\n')
        def result = gradle.withProjectDir(buildScript.parentFile).build()
        assert result.task(':test').outcome == SUCCESS
    }

    @Test
    void hasSoxExtension() {
        def buildScript = initBuildScript()
        buildScript << [
                'task test << {',
                '  assert sox',
                '}',
                'defaultTasks "test"'
        ].join('\n')
        def result = gradle.withProjectDir(buildScript.parentFile).build()
        assert result.task(':test').outcome == SUCCESS
    }
}
