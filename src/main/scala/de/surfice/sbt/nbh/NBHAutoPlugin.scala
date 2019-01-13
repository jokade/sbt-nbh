// Copyright (c) 2018. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sbt.nbh

import sbt.{Def, _}
import Keys._
import de.surfice.sbt.nbh.pkgconfig.NBHPkgConfigPlugin

object NBHAutoPlugin extends AutoPlugin {
  override def requires = NBHPkgConfigPlugin

  import scala.scalanative.sbtplugin.ScalaNativePlugin.autoImport._
  import NBHPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    nativeCompileOptions ++= nbhNativeCompileOptions.value,
    nativeLinkingOptions ++= nbhNativeLinkingOptions.value
  )
}