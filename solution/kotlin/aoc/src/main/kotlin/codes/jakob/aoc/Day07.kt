package codes.jakob.aoc

import codes.jakob.aoc.Day07.Directory
import codes.jakob.aoc.Day07.File
import codes.jakob.aoc.shared.splitMultiline
import codes.jakob.aoc.shared.toPair

class Day07 : Solution() {
    override fun solvePart1(input: String): Any {
        val root: Directory = buildDirectoryTree(input)
        val directoryByRecursiveSize: Map<Directory, Long> = calculateDirectorySizes(root)
        return directoryByRecursiveSize.values.filter { it <= SMALL_DIRECTORIES_MAX_SIZE }.sum()
    }

    override fun solvePart2(input: String): Any {
        val root: Directory = buildDirectoryTree(input)
        val directoryByRecursiveSize: Map<Directory, Long> = calculateDirectorySizes(root)
        val missingSpace: Long = MIN_UNUSED_SPACE - (TOTAL_DISK_SIZE - root.recursiveFileSize())
        return directoryByRecursiveSize.values.filter { it >= missingSpace }.minBy { it - missingSpace }
    }

    private fun calculateDirectorySizes(root: Directory): Map<Directory, Long> {
        return root.allRecursiveChildren().associateWith { it.recursiveFileSize() }
    }

    private fun buildDirectoryTree(input: String): Directory {
        val root = Directory(name = "/", parent = null)
        var currentDirectory: Directory = root
        for (command: Command in parse(input.splitMultiline())) {
            when (command) {
                is ChangeDirectory -> {
                    currentDirectory = if (command.path == root.name) {
                        root
                    } else {
                        if (command.path == ONE_LEVEL_BACK) {
                            currentDirectory.parent!!
                        } else {
                            currentDirectory.children.first { it.name == command.path }
                        }
                    }
                }

                is ListDirectory -> {
                    currentDirectory.children = command.directories().map { Directory(it, currentDirectory) }
                    currentDirectory.files = command.files()
                }
            }
        }
        return root
    }

    private fun parse(lines: List<String>): List<Command> {
        val linesIndexed: List<Pair<String, Int>> = lines.withIndex().map { it.value to it.index }
        val parsed: MutableList<Command> = mutableListOf()
        var index = 0
        while (index < linesIndexed.size) {
            val (line, lineIndex) = linesIndexed[index]
            if (line.startsWith("\$ cd")) {
                parsed += ChangeDirectory(line.substringAfterLast(" "))
                index++
            } else if (line == "\$ ls") {
                val takeWhile: List<Pair<String, Int>> =
                    linesIndexed.drop(index + 1).takeWhile { !it.first.startsWith("$") }
                parsed += ListDirectory(takeWhile.map { it.first })
                index = takeWhile.map { it.second }.last() + 1
            }
        }
        return parsed
    }

    data class Directory(
        val name: String, val parent: Directory?
    ) {
        lateinit var children: List<Directory>
        lateinit var files: List<File>

        fun recursiveFileSize(): Long {
            fun recurse(directory: Directory): Long {
                val combinedFileSize: Long = directory.files.combinedFileSize()
                return if (directory.children.isEmpty()) {
                    combinedFileSize
                } else {
                    combinedFileSize + directory.children.sumOf { recurse(it) }
                }
            }

            return recurse(this)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Directory) return false

            if (name != other.name) return false
            if (parent != other.parent) return false

            return true
        }

        override fun hashCode(): Int {
            var result: Int = name.hashCode()
            result = 31 * result + (parent?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Directory(name='$name', parent=${parent?.name})"
        }
    }

    data class File(
        val name: String, val size: Long
    )

    open class Command

    data class ChangeDirectory(
        val path: String
    ) : Command()

    data class ListDirectory(
        val output: List<String>
    ) : Command() {
        fun directories(): List<String> {
            return output.filter { it.startsWith("dir") }.map { it.split(" ").last() }
        }

        fun files(): List<File> {
            return output.filterNot { it.startsWith("dir") }.map { it.split(" ").toPair() }
                .map { (size, name) -> File(name, size.toLong()) }
        }
    }

    companion object {
        private const val ONE_LEVEL_BACK = ".."
        private const val SMALL_DIRECTORIES_MAX_SIZE = 100_000L
        private const val TOTAL_DISK_SIZE = 70_000_000L
        private const val MIN_UNUSED_SPACE = 30_000_000L
    }
}

private fun List<File>.combinedFileSize(): Long = sumOf { it.size }

private fun Directory.allRecursiveChildren(): Set<Directory> {
    fun recurse(directory: Directory): List<Directory> {
        return if (directory.children.isEmpty()) {
            emptyList()
        } else {
            directory.children + directory.children.flatMap { recurse(it) }
        }
    }

    return recurse(this).toSet()
}

fun main() = Day07().solve()
