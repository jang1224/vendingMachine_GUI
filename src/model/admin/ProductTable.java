package model.admin;

import javax.swing.table.AbstractTableModel;
import model.Product;
import model.Inventory;
import java.util.List;
import java.util.ArrayList;

public class ProductTable extends AbstractTableModel{
    private final String[] columnNames = {"이름", "가격", "재고", "이미지 경로"};
    private List<Product> productList;

    public ProductTable(Inventory inventory){
        this.productList = new ArrayList<>(inventory.getProductMap().values());
    }

    public void refreshData(Inventory inventory){
        this.productList = new ArrayList<>(inventory.getProductMap().values());
        fireTableDataChanged(); // 테이블 뷰에 데이터 변경 알림
    }

    public Product getProductAt(int rowIndex){    // 선택된 튜플의 Product 객체를 반환
        return productList.get(rowIndex);
    }
    @Override
    public int getColumnCount(){    // 속성 개수 출력
        return columnNames.length;
    }
    @Override
    public int getRowCount(){   // 행 개수 출력
        return productList.size();
    }
    @Override
    public String getColumnName(int col){   // 속성명 출력
        return columnNames[col];
    }
    @Override
    public Object getValueAt(int row, int col){
        Product product = productList.get(row);
        return switch (col) {
            case 0 -> product.getName();
            case 1 -> product.getPrice();
            case 2 -> product.getQuantity();
            case 3 -> product.getImagePath();
            default -> null;
        };

    }
}