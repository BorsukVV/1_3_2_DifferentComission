package ru.netology

val cardTypeVisa = "Visa"
val cardTypeMasterCard = "MasterCard"
val cardTypeMaestro = "Maestro"
val cardTypeVKPay = "VKPay"
val cardTypeMir = "Мир"
val periodMonth = "Month"
val periodDay = "Day"

fun main() {

    val transferIn = true
    val transferOut = false

    transfer(transferIn, cardTypeVKPay, periodDay, 16_000_00, 80_000_00)
    transfer(transferIn, cardTypeVKPay, periodDay, 10_000_00, 80_000_00)
    transfer(transferIn, cardTypeVKPay, periodDay, 10_000_00, 25_000_00)
    transfer(transferOut, cardTypeMaestro, periodMonth, 76_000_00, 80_000_00)
    transfer(transferOut, cardTypeMaestro, periodMonth, 55_000_00, 600_000_00)
    transfer(transferOut, cardTypeMaestro, periodMonth, 176_000_00, 80_000_00)
    transfer (transferIn, cardTypeVisa, periodMonth, 76_000_00, 80_000_00)

}

fun transfer(
    transferIn: Boolean,
    cardType: String,
    LimitType: String,
    dayTransferAmount: Int,
    monthTransferAmount: Int
) {

    when {
        cashTransferPossible(transferIn, cardType, LimitType, dayTransferAmount, monthTransferAmount) -> {
            val comission = calculateComission(cardType, monthTransferAmount, dayTransferAmount)
            val totalSum = dayTransferAmount + comission
            if (transferIn) {
                println("Зачисление на сумму: ${convertPriceToString(dayTransferAmount)}")
                printMessage (cardType, comission, totalSum)
            } else {
                println("Списание на сумму: ${convertPriceToString(dayTransferAmount)}")
                printMessage (cardType, comission, totalSum)
            }
        }
        (transferIn && LimitType == periodDay) || (transferIn && LimitType == periodMonth) -> {
            println("Вы превысили лимит на зачисление средств")
        }
        (!transferIn && LimitType == periodDay) || (!transferIn && LimitType == periodMonth) -> {
            println("Вы превысили дневной лимит на средств")
        }
        else -> println("Что-то пошло не так")
    }
}

fun printMessage (cardType: String, comission: Int, totalSum: Int) {
    println("Счёт: $cardType")
    println("Комиссия: ${convertPriceToString(comission)}")
    println("Итого: ${convertPriceToString(totalSum)}")
}

fun cashTransferPossible(
    transferIn: Boolean = true,
    cardType: String = cardTypeVKPay,
    LimitType: String = periodDay,
    dayTransferAmount: Int,
    monthTransferAmount: Int
): Boolean {

    val cashflowInLimitDay = 150_000_00
    val cashflowInLimitMonth = 600_000_00
    val cashflowOutLimitDay = 150_000_00
    val cashflowOutLimitMonth = 600_000_00
    val cashflowVKLimitDay = 15_000_00
    val cashflowVKLimitMonth = 40_000_00

    return when {
        cardType == cardTypeVKPay -> {
            cashLimitCheck(cashflowVKLimitDay, cashflowVKLimitMonth, dayTransferAmount, monthTransferAmount)
        }
        cardType != cardTypeVKPay && transferIn -> {
            cashLimitCheck(cashflowInLimitDay, cashflowInLimitMonth, dayTransferAmount, monthTransferAmount)
        }
        cardType != cardTypeVKPay && !transferIn -> {
            cashLimitCheck(cashflowOutLimitDay, cashflowOutLimitMonth, dayTransferAmount, monthTransferAmount)
        }
        else -> false
    }
}
    fun cashLimitCheck(
        limitDay: Int,
        limitMonth: Int,
        transferDay: Int,
        transferMonth: Int,
    ): Boolean {
        return transferDay < limitDay && transferMonth < limitMonth
    }

    fun calculateComission(
        cardType: String = cardTypeVKPay,
        monthTransferAmount: Int = 0,
        transfer: Int
    ): Int {
        val monthTransferLimitMaestroMasterCard = 75_000_00

        return when (cardType) {
            cardTypeMasterCard, cardTypeMaestro -> {
                if (monthTransferAmount > monthTransferLimitMaestroMasterCard) {
                    mastrercardMaestroComissionCalculator(transfer)
                } else 0
            }
            cardTypeMir, cardTypeVisa -> {
                visaMirComissionCalculator(transfer)
            }
            else -> 0
        }
    }

    fun mastrercardMaestroComissionCalculator(transfer: Int): Int {
        val comissionPercent = 0.006
        val comissionTaxRub = 20_00
        return (transfer * comissionPercent + comissionTaxRub).toInt()
    }

    fun visaMirComissionCalculator(transfer: Int): Int {
        val comissionPercent = 0.0075
        val comissionTaxMin = 35_00
        val comission = (transfer * comissionPercent).toInt()
        return if (comission <= comissionTaxMin) comissionTaxMin else comission
    }

    fun convertPriceToString(sum: Int): String {
        val sumValueRub = sum / 100
        val sumValueCop = sum % 100
        val sumValueCopFormatted: String = String.format("%02d", sumValueCop)
        return sumValueRub.toString() + " руб. " + sumValueCopFormatted + "коп."
    }


