package model.user;

import model.Inventory;
import model.Product;

public class VMProcessor {
    private final Inventory inventory;

    public VMProcessor(Inventory inventory){
        this.inventory = inventory;
    }

    public String processPurchase(Product product, AccountManager currentBalance){
        // 현금 결제인 경우에만 검사 수행
        if(!currentBalance.getIsCard()) {
            int productQuantity = product.getQuantity();
            // 재고 부족 체크
            if (productQuantity <= 0) {
                return "재고 부족 : " + product.getName();
            }
            // 잔액 부족 체크
            if (currentBalance.getCurrentBalance() < product.getPrice()) {
                return "잔액 부족 : " + (product.getPrice() - currentBalance.getCurrentBalance()) + "원이 더 필요합니다.";
            }
            // 검사 통과 시 AccountManager를 통해 잔액 차감
            currentBalance.deductAmount(product.getPrice());    // 잔액 - 상품 가격
        }
        // 구매 성공 : Inventory를 통해 DB 재고 1 감소
        inventory.decreaseProduct(product);
        return product.getName() + " 구매 성공!";
    }

    public String getReturnChange(AccountManager currentBalance){
        return currentBalance.returnChange() + "원이 반환되었습니다.";
    }

}
