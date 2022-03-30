package ru.netology

const val cardTypeVisa = "Visa"
const val cardTypeMasterCard = "MasterCard"
const val cardTypeMaestro = "Maestro"
const val cardTypeVKPay = "VKPay"
const val cardTypeMir = "Мир"
const val periodMonth = "Month"
const val periodDay = "Day"

fun main() {

    val commissionForIncomingTransfers = 0

//    val transferIn = true
//    val cardType = cardTypeVKPay
//    val dayTransferAmount = 12_000_00
//    val monthTransferAmount = 30_000_00
//
//    val transferIn = true
//    val cardType = cardTypeVKPay
//    val dayTransferAmount = 12_000_00
//    val monthTransferAmount = 80_000_00
//
//    val transferIn = true
//    val cardType = cardTypeVKPay
//    val dayTransferAmount = 16_000_00
//    val monthTransferAmount = 30_000_00
//
//    val transferIn = true
//    val cardType = cardTypeVisa
//    val dayTransferAmount = 12_000_00
//    val monthTransferAmount = 30_000_00
//
//    val transferIn = false
//    val cardType = cardTypeVisa
//    val dayTransferAmount = 180_000_00
//    val monthTransferAmount = 30_000_00
//
//    val transferIn = false
//    val cardType = cardTypeVisa
//    val dayTransferAmount = 12_000_00
//    val monthTransferAmount = 700_000_00

    val transferIn = false
    val cardType = cardTypeMasterCard
    val dayTransferAmount = 12_000_00
    val monthTransferAmount = 85_000_00

//    val transferIn = false
//    val cardType = cardTypeMasterCard
//    val dayTransferAmount = 85_000_00
//    val monthTransferAmount = 30_000_00

//    val transferIn = false
//    val cardType = cardTypeMasterCard
//    val dayTransferAmount = 200_000_00
//    val monthTransferAmount = 30_000_00

    val isPossibleDay = cashTransferPossible(transferIn, cardType, dayTransferAmount, periodDay)
    val isPossibleMonth = cashTransferPossible(transferIn, cardType, monthTransferAmount, periodMonth)

    when {
        isPossibleDay && isPossibleMonth -> {
            //если перевод возможен, то:
            val comission: Int
            val totalSum: Int
            if (transferIn) {
                comission = commissionForIncomingTransfers
                totalSum = dayTransferAmount + comission
                println("Зачисление на сумму: ${convertPriceToString(dayTransferAmount)}")
                printMessage(cardType, comission, totalSum)
            } else {
                comission = calculateComission(transferIn, cardType, monthTransferAmount, dayTransferAmount)
                totalSum = dayTransferAmount + comission
                println("Списание на сумму: ${convertPriceToString(dayTransferAmount)}")
                printMessage(cardType, comission, totalSum)
            }
        }
        (transferIn && !isPossibleDay) -> {
            println("Вы превысили дневной лимит на зачисление средств")
        }
        (!transferIn && !isPossibleDay) -> {
            println("Вы превысили дневной лимит на списание средств")
        }
        (transferIn && !isPossibleMonth) -> {
            println("Вы превысили лимит на зачисление средств в месяц")
        }
        (!transferIn && !isPossibleMonth) -> {
            println("Вы превысили лимит на списание средств в месяц")
        }
        else -> {
            println("Что-то пошло не так")
        }
    }

}

fun printMessage(cardType: String, comission: Int, totalSum: Int) {
    println("Счёт: $cardType")
    println("Комиссия: ${convertPriceToString(comission)}")
    println("Итого: ${convertPriceToString(totalSum)}")
}

fun cashTransferPossible(
    transferIn: Boolean = true,
    cardType: String = cardTypeVKPay,
    transferSum: Int,
    limitType: String
): Boolean {

    val cashflowInLimitDay = 150_000_00
    val cashflowInLimitMonth = 600_000_00
    val cashflowOutLimitDay = 150_000_00
    val cashflowOutLimitMonth = 600_000_00
    val cashflowVKLimitDay = 15_000_00
    val cashflowVKLimitMonth = 40_000_00

    return when {
        cardType == cardTypeVKPay -> {
            if (limitType == periodDay) {
                transferSum < cashflowVKLimitDay
            } else {
                transferSum < cashflowVKLimitMonth
            }
        }
        cardType != cardTypeVKPay && transferIn -> {
            if (limitType == periodDay) {
                transferSum < cashflowInLimitDay
            } else {
                transferSum < cashflowInLimitMonth
            }
        }
        cardType != cardTypeVKPay && !transferIn -> {
            if (limitType == periodDay) {
                transferSum < cashflowOutLimitDay
            } else {
                transferSum < cashflowOutLimitMonth
            }
        }
        else -> false
    }
}

fun calculateComission(
    transferIn: Boolean = true,
    cardType: String = cardTypeVKPay,
    monthTransferAmount: Int = 0,
    transfer: Int
): Int {
    val monthTransferLimitMaestroMasterCard = 75_000_00

    return if (!transferIn) {
        when (cardType) {
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
    } else 0

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


