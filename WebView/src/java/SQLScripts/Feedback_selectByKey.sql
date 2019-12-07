SELECT pf.account_id, pf.software_id, a.e_mail, res.title, res.class_name, pf.message
FROM "Product Feedback" pf 
JOIN "Account" a ON pf.account_id = a.account_id AND a.e_mail = ?
JOIN (SELECT s.software_id, s.title, sc.class_name FROM "Software" s 
     JOIN "Software Class" sc ON s.class_id = sc.class_id
     WHERE s.title = ? AND sc.class_name= ? ) res 
ON pf.software_id = res.software_id ;
