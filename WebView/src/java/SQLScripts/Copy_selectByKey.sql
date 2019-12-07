SELECT dc.copy_id, dc.purchase_id, pur.pay_code, dc.product_key, soft.title, soft.class_name 
FROM "Digital Copy" dc
    LEFT JOIN (Select p.purchase_id, o.pay_code 
    FROM "Purchase" p JOIN "Order" o ON p.order_id = o.order_id) pur 
ON dc.purchase_id = pur.purchase_id 
    JOIN (Select s.software_id,s.title, sc.class_name 
    FROM "Software" s JOIN "Software Class" sc ON s.class_id = sc.class_id 
    WHERE s.title = ? AND sc.class_name = ?) soft 
ON dc.software_id = soft.software_id 
WHERE dc.product_key = ?;