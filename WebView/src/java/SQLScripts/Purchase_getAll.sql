SELECT p.purchase_id, o.pay_code, soft.title, soft.class_name, "count", price_single 
FROM "Purchase" p 
JOIN "Order" o ON p.order_id = o.order_id 
JOIN (SELECT s.software_id, s.title, sc.class_name 
FROM "Software" s JOIN "Software Class" sc ON s.class_id = sc.class_id) soft 
ON p.software_id = soft.software_id 
ORDER BY o.pay_code;