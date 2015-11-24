package com.klgleb.yandexmoney.model;

import com.google.gson.annotations.SerializedName;

/**
 * total	amount	Общий баланс счета.
 available	amount	Сумма доступная для расходных операций.
 deposition_pending	amount	Сумма стоящих в очереди пополнений. Если зачислений в очереди нет, то параметр отсутствует.
 blocked	amount	Сумма заблокированных средств по решению исполнительных органов. Если заблокированных средств нет то параметр отсутствует.
 debt	amount	Сумма задолженности (отрицательного остатка на счете). Если задолженности нет, то параметр отсутствует.
 hold
 * Created by klgleb on 21.11.15.
 */
public class BalanceDetails {

    float total;
    float available;

    @SerializedName("deposition_pending")
    float depositionPending;

    float blocked;
    float debt;
    float hold;

    public BalanceDetails(float total, float available, float depositionPending, float blocked, float debt, float hold) {
        this.total = total;
        this.available = available;
        this.depositionPending = depositionPending;
        this.blocked = blocked;
        this.debt = debt;
        this.hold = hold;
    }


    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getAvailable() {
        return available;
    }

    public void setAvailable(float available) {
        this.available = available;
    }

    public float getDepositionPending() {
        return depositionPending;
    }

    public void setDepositionPending(float depositionPending) {
        this.depositionPending = depositionPending;
    }

    public float getBlocked() {
        return blocked;
    }

    public void setBlocked(float blocked) {
        this.blocked = blocked;
    }

    public float getDebt() {
        return debt;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }

    public float getHold() {
        return hold;
    }

    public void setHold(float hold) {
        this.hold = hold;
    }
}
