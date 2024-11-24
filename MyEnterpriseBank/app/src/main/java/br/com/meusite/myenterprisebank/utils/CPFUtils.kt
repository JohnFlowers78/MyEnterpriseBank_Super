package br.com.meusite.myenterprisebank.utils

object CPFUtils {
    /**
     * Valida se o CPF é válido.
     * @param cpf CPF (com ou sem máscara).
     * @return true se for válido, false caso contrário.
     */
    fun isValidCPF(cpf: String): Boolean {
        val sanitizedCPF = cpf.filter { it.isDigit() }

        if (sanitizedCPF.length != 11 || sanitizedCPF.all { it == sanitizedCPF[0] }) return false

        val firstCheckSum = (0..8).sumOf { sanitizedCPF[it].digitToInt() * (10 - it) }
        val firstVerifier = (11 - (firstCheckSum % 11)).let { if (it >= 10) 0 else it }
        if (sanitizedCPF[9].digitToInt() != firstVerifier) return false

        val secondCheckSum = (0..9).sumOf { sanitizedCPF[it].digitToInt() * (11 - it) }
        val secondVerifier = (11 - (secondCheckSum % 11)).let { if (it >= 10) 0 else it }
        return sanitizedCPF[10].digitToInt() == secondVerifier
    }
}