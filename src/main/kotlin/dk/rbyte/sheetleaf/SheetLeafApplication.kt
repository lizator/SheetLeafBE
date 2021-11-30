package dk.rbyte.sheetleaf

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SheetLeafApplication

fun main(args: Array<String>) {
    SpringApplication.run(SheetLeafApplication::class.java, *args)
}
