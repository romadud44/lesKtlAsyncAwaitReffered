import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * В функции main:
 *
 * 1. Получить списки целых чисел и символов с помощью функций getRandomList(), состоящие из 10 элементов.
 *
 * 2. Оба списка необходимо асинхронно распаковать.
 *
 * 3. Вывести в консоль общий размер полученного списка.
 *
 * 4. Объединить их функцией concatenate() и вывести в консоль полученный список.
 *
 *
 * 5. Посчитать общее время затраченных операций.
 */
suspend fun main() = runBlocking {
    val randomListOfInteger = getRandomListOfIntegers(10)
    println("Получен список 1 - $randomListOfInteger")
    val randomListOfChars = getRandomListOfChars(10)
    println("Получен список 2 - $randomListOfChars")

    val time = measureTimeMillis {
        val countsOfListOfInteger = async { unpack(randomListOfInteger) }
        val countsOfListOfChars = async { unpack(randomListOfChars) }
        println(
            "Общий размер:\n" +
                    "Список 1 - ${countsOfListOfInteger.await()} элементов\n" +
                    "Список 2 - ${countsOfListOfChars.await()} элементов"
        )
    }
    println("Объединённый список:\n${randomListOfInteger.concatenate(randomListOfChars)}")
    println("Затрачено времени: $time мс.")
}

/**
 * Написать функции getRandomList() создания списков рандомных чисел и символов. Данные функции могут быть перегруженными.
 */
fun getRandomListOfIntegers(elements: Int): List<Int> {

    val resultList = mutableListOf<Int>()
    for (i in 0..<elements) {
        resultList.add(i, (1..100).random())
    }
    return resultList
}

fun getRandomListOfChars(elements: Int): List<Char> {

    val resultList = mutableListOf<Char>()
    for (i in 0..<elements) {
        resultList.add(i, ('a'..'z').random())
    }
    return resultList
}

/**
 * Написать функцию распаковки списков unpack(), которые получаем из функций, описанных выше. Она обобщенная, т.е.
 * может принимать список разных типов. Функция выводит в консоль элементы, но все через секундную задержку и в процессе
 * ведет подсчет количества элементов (для имитации длительной работы). Возвращает количество элементов списка.
 */
suspend fun <T> unpack(listIn: List<T>): Int {
    var count = 0
    for (i in listIn.indices) {
        delay(1000L)
        println(listIn[i])
        count++
        println("Идёт распаковка, количество элементов в списке: $count")
    }

    return count
}

/**
 * Также есть функция, объединяющая списки в один concatenate(). Учитывайте, что списки могут быть разных типов.
 * Она возвращает объект Pair<Int, MutableList<T>>, где Int – количество элементов объединенного списка, MutableList<T>
 *     - сам список.
 */
fun <T> List<T>.concatenate(inputList: List<T>): Pair<Int, MutableList<T>> {
    val countOfList = this.count() + inputList.count()
    val newList = this.toMutableList()
    for (i in inputList.indices) {
        newList.add(inputList[i])
    }
    val resultList = Pair(countOfList, newList)
    return resultList
}