package model;

public class AccountManager {
    private int currentBalance = 0;
    public void insertCoinOrCash(int amount){ // 동전이나 현금 투입
        this.currentBalance += amount;
    }
    public int getCurrentBalance(){ // 잔액 조회
        return this.currentBalance;
    }
    public void deductAmount(int price){   // 금액 차감
        this.currentBalance -= price;
    }
    public int returnChange(){  // 동전 및 현금 반환
        int change = this.currentBalance;
        this.currentBalance = 0;
        return change;
    }
}
