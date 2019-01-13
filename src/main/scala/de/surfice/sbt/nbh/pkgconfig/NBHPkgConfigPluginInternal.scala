// Copyright (c) 2018. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sbt.nbh.pkgconfig

import sbt._
import Keys._
import de.surfice.sbt.nbh.Utils._
import collection.JavaConverters._

import scala.scalanative.sbtplugin.Utilities.SilentLogger

object NBHPkgConfigPluginInternal {
  import NBHPkgConfigPlugin.autoImport._
  import de.surfice.sbt.nbh.NBHPlugin.autoImport._
  import de.surfice.sbt.pconf.PConfPlugin.autoImport._

  val nbhPkgConfigLinkingFlags: TaskKey[Seq[String]] =
    taskKey[Seq[String]]("result of `pkg-config --libs ${nbhPkgConfigModules}`")

  val nbhPkgConfigModulesComputed: TaskKey[Seq[String]] =
    taskKey[Seq[String]]("pkg-config modules defined explicitly and in package-conf files")


  lazy val projectSettings: Seq[Setting[_]] = Seq(
    nbhPkgConfig := discover("pkg-config"),
    nbhPkgConfigModules := Nil,

    nbhPkgConfigModulesComputed := {
      nbhPkgConfigModules.value ++ pconfConfig.value.getStringList("nbh.pkgConfig.modules").asScala
    },

    nbhPkgConfigLinkingFlags := {
      Process(Seq(nbhPkgConfig.value.absolutePath, "--libs") ++ nbhPkgConfigModulesComputed.value)
        .lines_!(SilentLogger)
        .flatMap(_.split(" "))
    },

    nbhNativeLinkingOptions := {
      nbhNativeLinkingOptions.value ++ nbhPkgConfigLinkingFlags.value
    }
  )

}