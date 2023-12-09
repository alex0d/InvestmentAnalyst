package ru.alex0d.investmentanalyst

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InvestmentAnalystApplication

fun main(args: Array<String>) {
    runApplication<InvestmentAnalystApplication>(*args)
}
