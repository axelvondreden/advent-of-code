package y2020

import Day

class Day17 : Day(2020, 17) {

    override val input = readCharMatrix()

    override fun solve1(): Int {
        var cubes = parseCubes(input)
        repeat(6) {
            cubes = cubes.step()
        }
        return cubes.size
    }

    override fun solve2(): Int {
        var cubes = parseHyperCubes(input)
        repeat(6) {
            cubes = cubes.step()
        }
        return cubes.size
    }

    private fun Set<Cube>.step(): Set<Cube> {
        val xRange = (minByOrNull { it.x }!!.x - 1)..(maxByOrNull { it.x }!!.x + 1)
        val yRange = (minByOrNull { it.y }!!.y - 1)..(maxByOrNull { it.y }!!.y + 1)
        val zRange = (minByOrNull { it.z }!!.z - 1)..(maxByOrNull { it.z }!!.z + 1)
        val new = mutableSetOf<Cube>()
        xRange.forEach { x ->
            yRange.forEach { y ->
                zRange.forEach { z ->
                    val cube = Cube(x, y, z)
                    val neighbours = countNeighbours(cube)
                    if (cube in this) {
                        if (neighbours in 2..3) new.add(cube)
                    } else if (neighbours == 3) new.add(cube)
                }
            }
        }
        return new
    }

    @JvmName("stepHyperCube")
    private fun Set<HyperCube>.step(): Set<HyperCube> {
        val xRange = (minByOrNull { it.x }!!.x - 1)..(maxByOrNull { it.x }!!.x + 1)
        val yRange = (minByOrNull { it.y }!!.y - 1)..(maxByOrNull { it.y }!!.y + 1)
        val zRange = (minByOrNull { it.z }!!.z - 1)..(maxByOrNull { it.z }!!.z + 1)
        val wRange = (minByOrNull { it.w }!!.w - 1)..(maxByOrNull { it.w }!!.w + 1)
        val new = mutableSetOf<HyperCube>()
        xRange.forEach { x ->
            yRange.forEach { y ->
                zRange.forEach { z ->
                    wRange.forEach { w ->
                        val cube = HyperCube(x, y, z, w)
                        val neighbours = countNeighbours(cube)
                        if (cube in this) {
                            if (neighbours in 2..3) new.add(cube)
                        } else if (neighbours == 3) new.add(cube)
                    }
                }
            }
        }
        return new
    }

    private fun Set<Cube>.countNeighbours(cube: Cube) =
        ((cube.x - 1)..(cube.x + 1)).sumOf { x ->
            ((cube.y - 1)..(cube.y + 1)).sumOf { y ->
                ((cube.z - 1)..(cube.z + 1)).count { (x != cube.x || y != cube.y || it != cube.z) && Cube(x, y, it) in this }
            }
        }

    private fun Set<HyperCube>.countNeighbours(cube: HyperCube) =
        ((cube.x - 1)..(cube.x + 1)).sumOf { x ->
            ((cube.y - 1)..(cube.y + 1)).sumOf { y ->
                ((cube.z - 1)..(cube.z + 1)).sumOf { z ->
                    ((cube.w - 1)..(cube.w + 1)).count {
                        (x != cube.x || y != cube.y || z != cube.z || it != cube.w) && HyperCube(x, y, z, it) in this
                    }
                }
            }
        }

    private fun parseCubes(input: Array<CharArray>) = input.withIndex().flatMap { x ->
        input[x.index].withIndex().filter { it.value == '#' }.map { y -> Cube(x.index, y.index, 0) }
    }.toSet()

    private fun parseHyperCubes(input: Array<CharArray>) = input.withIndex().flatMap { x ->
        input[x.index].withIndex().filter { it.value == '#' }.map { y -> HyperCube(x.index, y.index, 0, 0) }
    }.toSet()

    private data class Cube(val x: Int, val y: Int, val z: Int)

    private data class HyperCube(val x: Int, val y: Int, val z: Int, val w: Int)
}