package kraski.server

import kraski.common.interpretation.DirRef
import kraski.common.interpretation.FileRef
import java.io.File

fun File.pave() = this.also { d -> if (!d.exists()) d.mkdirs() }

val DirRef.file
    get() = File(path.trim('/'))

val FileRef.file
    get() = File(path.trim('/'))

val DirRef.liveFile
    get() = file.pave()

val FileRef.liveFile
    get() = file.also { it.parentFile.pave() }
