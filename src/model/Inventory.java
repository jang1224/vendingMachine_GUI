package model;
import java.util.HashMap;

public class Inventory {
    private final HashMap<String, Product> productMap = new HashMap<>();
    public Inventory(){
        productMap.put("코카콜라",new Product("코카콜라", 1200, 10, "image/cocaCola.png"));
        productMap.put("칠성사이다",new Product("칠성사이다", 1200, 10, "image/cider.png"));
        productMap.put("레쓰비",new Product("레쓰비", 900, 10,"image/letsBe.png"));
        productMap.put("밀키스",new Product("밀키스", 1100, 10,"image/milkis.png"));
        productMap.put("코카콜라 제로",new Product("코카콜라 제로", 1200, 10,"image/cocaColaZero.png"));
        productMap.put("데자와",new Product("데자와", 900, 10,"image/tejava.png"));
        productMap.put("환타",new Product("환타", 1100, 10,"image/fanta.png"));
        productMap.put("물",new Product("물", 600, 10,"image/water.png"));
        productMap.put("하늘보리",new Product("하늘보리", 1300, 10,"image/haneulBori.png"));
        productMap.put("핫식스",new Product("핫식스", 800, 10,"image/hox6.png"));
        productMap.put("초코송이",new Product("초코송이", 1000, 10,"image/chocoSongi"));
        productMap.put("칸쵸",new Product("칸쵸", 1100, 10,"image/kancho.png"));
        productMap.put("버터와플",new Product("버터와플", 1500, 10,"image/butterWaffle.png"));
        productMap.put("오레오",new Product("오레오", 1200, 10,"image/oreo.png"));
        productMap.put("빼빼로",new Product("빼빼로", 1000, 10,"image/pepero.png"));
        productMap.put("쿠쿠다스",new Product("쿠쿠다스", 2000, 10,"image/kukudas.png"));
        productMap.put("가나초콜릿",new Product("가나초콜릿", 2000, 10,"image/ghanaChocolate.png"));
        productMap.put("허쉬초콜릿",new Product("허쉬초콜릿", 2500, 10,"image/hersheysChocolate.png"));
        productMap.put("쫄병",new Product("쫄병", 1600, 10,"image/jjolbyeong.png"));
        productMap.put("예감",new Product("예감", 1400, 10,"image/yegam.png"));
        productMap.put("빈츠", new Product("빈츠", 1500, 10,"image/binch.png"));
//        JButton drink1 = new JButton("코카콜라");
//        JButton drink2 = new JButton("칠성사이다");
//        JButton drink3 = new JButton("레스비");
//        JButton drink4 = new JButton("밀키스");
//        JButton drink5 = new JButton("코카콜라 제로");
//        JButton drink6 = new JButton("데자와");
//        JButton drink7 = new JButton("환타");
//        JButton drink8 = new JButton("물");
//        JButton drink9 = new JButton("하늘보리");
//        JButton drink10 = new JButton("핫식스");
//        JButton snack1 = new JButton("초코송이");
//        JButton snack2 = new JButton("칸쵸");
//        JButton snack3 = new JButton("버터와플");
//        JButton snack4 = new JButton("오레오");
//        JButton snack5 = new JButton("빼빼로");
//        JButton snack6 = new JButton("쿠쿠다스");
//        JButton snack7 = new JButton("가나초콜릿");
//        JButton snack8 = new JButton("허쉬초콜릿");
//        JButton snack9 = new JButton("쫄병");
//        JButton snack10 = new JButton("예감");
    }
    public void decreaseProduct(Product product){
        int quantity = product.getQuantity();
        quantity -= 1;
        product.setQuantity(quantity);
        productMap.put(product.getName(),product);
    }
//    public Set<String> getAllProduct(){
//        return productMap.keySet();
//    }
    public HashMap<String, Product> getProductMap(){
        return productMap;
    }
}
