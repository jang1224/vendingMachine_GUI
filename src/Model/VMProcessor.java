package Model;

public class VMProcessor {
    private final Inventory inventory;

    public VMProcessor(Inventory inventory){
        this.inventory = inventory;
    }

    public String processPurchase(Product product, AccountManager currentBalance){
        int productQuantity = product.getQuantity();

        if (productQuantity <= 0){
            return "재고 부족 : " + product.getName();
        }
        if (currentBalance.getCurrentBalance() < product.getPrice()){
            return "잔액 부족 : " + (product.getPrice() - currentBalance.getCurrentBalance()) + "원이 더 필요합니다.";
        }
        // 구매 성공일때
        inventory.decreaseProduct(product); // 물량 1 감소
        currentBalance.deductAmount(product.getPrice());    // 잔액 - 상품 가격
        return product.getName() + " 구매 성공!";
    }

    public String getReturnChange(AccountManager currentBalance){
        return currentBalance.returnChange() + "원이 반환되었습니다.";
    }

}
