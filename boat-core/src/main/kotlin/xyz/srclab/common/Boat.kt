package xyz.srclab.common

import xyz.srclab.common.lang.About
import xyz.srclab.common.lang.Author
import xyz.srclab.common.lang.SemVer

object Boat {

    private val sunqian = Author.of(
        "Sun Qian", "fredsuvn@163.com", null
    )
    private val srclab = Author.of(
        "SrcLab", "srclab@163.com", "https://github.com/srclab-projects"
    )

    @get:JvmName("version")
    @JvmStatic
    val version: SemVer = SemVer.of(0, 0, 0)

    @get:JvmName("about")
    @JvmStatic
    val about: About = About.of(
        "Boat",
        version.toString(),
        listOf(sunqian, srclab),
        srclab.mail,
        "https://github.com/srclab-projects/boat",
        listOf("Apache 2.0 license"),
        emptyList(),
        "© 2021 SrcLab"
    )

    @get:JvmName("secretCodes")
    @JvmStatic
    val secretCodes: List<String> = listOf(
        "Thank you, Taro.",
        "谢谢你，泰罗。",
    )
}