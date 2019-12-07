/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var selected_table = "";
var selected_operation = "";
var nullable_map;
var numeric_map;
var date_arr;
var datetime_arr;
var optionable_map;
var key_map;
var $div_desc;
var $div_fields;
var $div_status;
var $button_perform;

function initBaseVars() {
    $div_desc = $("#dialog-description");
    $div_fields = $("#dialog-fields-place");
    $div_status = $("#dialog-status-place");
    $button_perform = $("#b_execute");
    nullable_map = new Map([['software Class', ['class_parent_name_old', 'class_parent_name', 'class_parent_name_new']],
        ["software", ['release_date', 'publisher_name']]]);
    numeric_map = new Map([['software', ['actual_price']],
        ["order", ['summary_price']],
        ["purchase", ['price_single', 'count']]]
    );
    date_arr = ['date_of_birth','release_date'];
    datetime_arr = ['order_datetime'];
    
    ProductListSelect();
}

function onAbortOp() {
    closeDialog();
}

function clearDialog(){
    $div_desc.empty();
    $div_fields.empty();
}

function closeDialog(){
    $("#cover-div").remove();
    $("#dialog-form-container").css("display", "none");
    $div_status.empty();
    window["click"+selected_table.charAt(0).toUpperCase()+selected_table.slice(1).replace(' ', '')]();
}

function openDialog(){
    clearDialog();
    $button_perform.text(selected_operation);
    $("<div>").attr("id", "cover-div").appendTo($('body'));
    $("<p>").appendTo($div_desc).text(
            selected_table.charAt(0).toUpperCase()+selected_table.slice(1)+" :  "+selected_operation
            );
    $("#dialog-form-container").css("display", "block");
}

function clickExecute(){
    $div_status.empty();
    checkPerform();
}

function checkPerform() {
    var $ul = $("<ul>").appendTo($div_status);
    var nullable_arr = nullable_map.get(selected_table);
    if (nullable_arr === undefined)
        nullable_arr = [];
    var numeric_arr = numeric_map.get(selected_table);
    if (numeric_arr === undefined)
        numeric_arr = [];
    var $inputs = $div_fields.find("input");
    var name;
    var isOk = true;
    var isSubmit = true;
    var object = {};
    $.each($inputs, function (index, $input) {
        name = $input.name;
        object[name] = $input.value;
        if (!nullable_arr.includes(name)) {
            if (!$input.value.trim()) {
                $("<li>").appendTo($ul).text(name + " can't be empty!");
                isOk = false;
            } else
                isOk = specificValid($ul, numeric_arr, $input, name);
        } else if ($input.value.trim()) {
            isOk = specificValid($ul, numeric_arr, $input, name);
        }
        if (!isOk){
            isSubmit = false;
        }
    });
    if (isSubmit){
        $.post("MainServlet",{op: selected_operation, tab: selected_table, json: JSON.stringify(object)}, function (responsejson){
           $("<li>").appendTo($div_status).text(responsejson.status);
           $("<li>").appendTo($div_status).text(responsejson.message);  
        }
        );
    }
}

function specificValid($ul, numeric_arr ,$input, name){
    if (numeric_arr.includes(name) && !($.isNumeric($input.value))) {
        $("<li>").appendTo($ul).text(name + " should be numeric!");
        return false;
    }if (date_arr.includes(name) && ($input.value.trim().length > 10 || isNaN(Date.parse($input.value)))) {
        $("<li>").appendTo($ul).text(name + " should have 'YYYY-MM-DD' DATE format!");
        return false;
    }if (datetime_arr.includes(name) && ($input.value.trim().length < 11 || isNaN(Date.parse($input.value)))) {
        $("<li>").appendTo($ul).text(name + " should have 'YYYY-MM-DD HOUR:MIN:SECOND'  DATE format!");
        return false;
    }
    return true;
}


function clickCreate(){
    selected_operation = "Create";
    openDialog();
    window[selected_table.replace(' ','')+selected_operation+'Builder']();
}

function clickUpdate(){
    selected_operation = "Update";
    openDialog();
    window[selected_table.replace(' ','')+selected_operation+'Builder']();
}

function clickDelete(){
    selected_operation = "Delete";
    openDialog();
    window[selected_table.replace(' ','')+selected_operation+'Builder']();
}

function openModal(){
    $("#modal_window").css("display","flex");
}

function clickAccount() {
    $.get("MainServlet",{tab: 'Account' }, function (responsejson) {
        e_mail_arr = [];
        owner_name_arr = [];
        password_arr = [];
        var $div = $("#db_table");
        $div.empty();
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("e_mail"))
                .append($("<th>").text("owner_name"))
                .append($("<th>").text("gender"))
                .append($("<th>").text("date_of_birth"))
                .append($("<th>").text("password"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.e_mail))
                    .append($("<td>").text(record.owner_name))
                    .append($("<td>").text(record.gender))
                    .append($("<td>").text(record.date_of_birth))
                    .append($("<td>").text(record.password));
            e_mail_arr.push(record.e_mail);
            if (!owner_name_arr.includes(record.owner_name)){
                owner_name_arr.push(record.owner_name);
            }
            if (!password_arr.includes(record.password)){
                password_arr.push(record.password);
            }
        });
        key_map = new Map([['e_mail_old', 1], ['e_mail', 1],
            ['owner_name', 2], ['password', 3]]
                );
        optionable_map = new Map([[1,e_mail_arr],[2,owner_name_arr],[3,password_arr]]);
        selected_table = "account";
    });
    openModal();
}

function accountCreateBuilder(){
    accountBuilder(['owner_name', 'date_of_birth', 'gender', 'e_mail','password'],[]);
}

function accountUpdateBuilder(){
    accountBuilder(['owner_name', 'date_of_birth', 'gender', 'e_mail_old', 'e_mail_new', 'password'],['owner_name','password','e_mail_old']);
}

function accountBuilder(fields,selectables){
    var $ul = $("<ul>").appendTo($div_fields);
    fields.forEach(function (field){
        var $li = $("<li>").appendTo($ul);
        
        if (field ==='gender') {   
            $("<input>").attr({type:'radio',id: "r_male",name: field, checked: 'true', value: 'M'}).appendTo($li);
            $("<label>").attr({id : "l_rad",for: "r_male"}).text("Male -").appendTo($li);
            $li = $("<li>").appendTo($ul);
            $("<input>").attr({type:'radio',id: "r_female", name: field, value: 'F'}).appendTo($li);
            $("<label>").attr({id : "l_rad",for: "r_female"}).text("Female -").appendTo($li);
        }
        else{
            $("<label>").attr('for', field).text(field+": ").appendTo($li);
            if (selectables.includes(field)){
               $("<datalist>").attr({id: field+"list"}).appendTo($li);
               $("<input>").attr({type:"text", list: field+"list", name: field}).appendTo($li);
               var options;
               optionable_map.get(key_map.get(field)).forEach(function(email){
                   options += '<option value="'+email+'">';
               });
               $('#'+field+'list').append(options);
            }
            else{
                $("<input>").attr({type:'text', name: field}).appendTo($li);
            }
            
        }
    });
}

function accountDeleteBuilder(){
    formBuilder(['e_mail'],['e_mail']);
}

function clickDigitalCopy() {
    $.get("MainServlet",{ tab: 'Digital_Copy' },  function (responsejson) {
        var $div = $("#db_table");
        var pay_code_arr = [];
        var title_arr = [];
        var class_name_arr = [];
        var product_key_arr = [];
        $div.empty();
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("pay_code"))
                .append($("<th>").text("title"))
                .append($("<th>").text("class_name"))
                .append($("<th>").text("product_key"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.pay_code))
                    .append($("<td>").text(record.title))
                    .append($("<td>").text(record.class_name))
                    .append($("<td>").text(record.product_key));
            if (!pay_code_arr.includes(record.pay_code)){
                pay_code_arr.push(record.pay_code);
            }
            if (!title_arr.includes(record.title)){
                title_arr.push(record.title);
            }
            if (!class_name_arr.includes(record.class_name)){
                class_name_arr.push(record.class_name);
            }
            if (!product_key_arr.includes(record.product_key)){
                product_key_arr.push(record.product_key);
            }
        });
        key_map = new Map([['pay_code',1], ['title_old',2], ['title_new',2],
         ['title',2], ['class_name_old',3], ['class_name_new',3], ['class_name',3],
         ['product_key_old', 4],['product_key_new', 4],['product_key', 4]]);
        optionable_map = new Map([[1,pay_code_arr],[2, title_arr],[3,class_name_arr],
            [4, product_key_arr]]);
        selected_table = "digital Copy";
    });
    openModal();
}

function digitalCopyCreateBuilder() {
    formBuilder(['pay_code', 'title','class_name', 'product_key'],['pay_code', 'title','class_name']);
}

function digitalCopyUpdateBuilder() {
    formBuilder(['pay_code', 'title_old', 'class_name_old','product_key_old',
        'title_new','class_name_new','product_key_new'],['pay_code', 'title_old', 'class_name_old','product_key_old',
        'title_new','class_name_new','product_key_new']);
}

function digitalCopyDeleteBuilder(){
    formBuilder(['title','class_name', 'product_key'],['title','class_name', 'product_key']);
}

function clickOrder() {
    $.get("MainServlet",{ tab: 'Order' },  function (responsejson) {
        var $div = $("#db_table");
        $div.empty();
        var e_mail_arr = [];
        var pay_code_arr = [];
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("e_mail"))
                .append($("<th>").text("order_datetime"))
                .append($("<th>").text("summary_price"))
                .append($("<th>").text("pay_code"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.e_mail))
                    .append($("<td>").text(record.order_datetime))
                    .append($("<td>").text(record.summary_price))
                    .append($("<td>").text(record.pay_code));
            if (!e_mail_arr.includes(record.e_mail)){
                e_mail_arr.push(record.e_mail);
            }
            if (!pay_code_arr.includes(record.pay_code)){
                pay_code_arr.push(record.pay_code);
            }
        });
        key_map = new Map([['e_mail',1], ['pay_code_old',2], ['pay_code',2]]);
        optionable_map = new Map([[1,e_mail_arr],[2, pay_code_arr]]);
        selected_table = "order";
    });
    openModal();
}

function orderCreateBuilder(){
    formBuilder(['e_mail', 'order_datetime','summary_price', 'pay_code'],['e_mail']);
}

function orderUpdateBuilder(){
    formBuilder(['e_mail', 'order_datetime','summary_price', 'pay_code_old', 'pay_code_new'],['e_mail','pay_code_old']);
}

function orderDeleteBuilder(){
    formBuilder(['pay_code'],['pay_code']);
}

function clickProductFeedback() {
    $.get("MainServlet",{ tab: 'Product_Feedback' },  function (responsejson) {
        var $div = $("#db_table");
        var e_mail_arr = [];
        var title_arr = [];
        var class_name_arr = [];
        $div.empty();
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("e_mail"))
                .append($("<th>").text("title"))
                .append($("<th>").text("class_name"))
                .append($("<th>").text("message"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.e_mail))
                    .append($("<td>").text(record.title))
                    .append($("<td>").text(record.class_name))
                    .append($("<td>").text(record.message));
            if (!e_mail_arr.includes(record.e_mail)){
                e_mail_arr.push(record.e_mail);
            }
            if (!title_arr.includes(record.title)){
                title_arr.push(record.title);
            }
            if (!class_name_arr.includes(record.class_name)){
                class_name_arr.push(record.class_name);
            }
        });
        key_map = new Map([['e_mail',1],['e_mail_old',1],['e_mail_new',1], ['pay_code_old',2], ['pay_code',2],
         ['title_old',2], ['title_new',2], ['title',2], ['class_name_old',3],
         ['class_name_new',3], ['class_name',3]]);
        optionable_map = new Map([[1,e_mail_arr],[2, title_arr],[3, class_name_arr]]);
        selected_table = "product Feedback";
    });
    openModal();
}

function productFeedbackCreateBuilder(){
    formBuilder(['e_mail','title','class_name','message'],['e_mail','title','class_name']);
}

function productFeedbackUpdateBuilder() {
    formBuilder(['e_mail_old','title_old','class_name_old','e_mail_new','title_new','class_name_new','message'],
    ['e_mail_old','title_old','class_name_old','e_mail_new','title_new','class_name_new']);
}

function productFeedbackDeleteBuilder() {
    formBuilder(['e_mail','title','class_name'],['e_mail','title','class_name']);
}

function clickPublisher() {
    $.get("MainServlet",{ tab: 'Publisher' },  function (responsejson) {
        var $div = $("#db_table");
        $div.empty();
        publisher_name_arr = [];
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("publisher_name"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.publisher_name));
            if (!publisher_name_arr.includes(record.publisher_name)){
                publisher_name_arr.push(record.publisher_name);
            }
        });
        key_map = new Map([['publisher_name',1], ['publisher_name_old',1], ['publisher_name_new',1]]);
        optionable_map = new Map([[1,publisher_name_arr]]);
        selected_table = "publisher";
    });
    openModal();
}

function publisherUpdateBuilder(){
    formBuilder(['publisher_name_old', 'publisher_name_new'],['publisher_name_old']);
}

function publisherCreateBuilder(){
    formBuilder(['publisher_name'],[]);
}

function publisherDeleteBuilder(){
    formBuilder(['publisher_name'],['publisher_name']);
}

function clickPurchase() {
    $.get("MainServlet",{ tab: 'Purchase' },  function (responsejson) {
        var $div = $("#db_table");
        $div.empty();
        var pay_code_arr = [];
        var title_arr = [];
        var class_name_arr = [];
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("pay_code"))
                .append($("<th>").text("title"))
                .append($("<th>").text("class_name"))
                .append($("<th>").text("count"))
                .append($("<th>").text("price_single"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.pay_code))
                    .append($("<td>").text(record.title))
                    .append($("<td>").text(record.class_name))
                    .append($("<td>").text(record.count))
                    .append($("<td>").text(record.price_single));
            if (!pay_code_arr.includes(record.pay_code)){
                pay_code_arr.push(record.pay_code);
            }
            if (!title_arr.includes(record.title)){
                title_arr.push(record.title);
            }
            if (!class_name_arr.includes(record.class_name)){
                class_name_arr.push(record.class_name);
            }
        });
        key_map = new Map([['pay_code',1],['pay_code_old',1],['pay_code_new',1],
         ['title_old',2], ['title_new',2], ['title',2], ['class_name_old',3],
         ['class_name_new',3], ['class_name',3]]);
        optionable_map = new Map([[1,pay_code_arr],[2, title_arr],[3, class_name_arr]]);
        selected_table="purchase";
    });
    openModal();
}

function purchaseCreateBuilder(){
    formBuilder(['pay_code','title', 'class_name', 'count','price_single'],['pay_code','title', 'class_name']);
}

function purchaseUpdateBuilder(){
    formBuilder(['pay_code_old','title_old','class_name_old','pay_code_new','title_new','class_name_new','count','price_single']
            ,['pay_code_old','title_old','class_name_old','pay_code_new','title_new','class_name_new']);
}

function purchaseDeleteBuilder(){
    formBuilder(['pay_code', 'title', 'class_name'],['pay_code', 'title', 'class_name']);
}

function clickSoftware() {
    $.get("MainServlet",{ tab: 'Software' },  function (responsejson) {
        var $div = $("#db_table");
        $div.empty();
        publisher_name_arr = [];
        class_name_arr = [];
        title_arr = [];
        esrb_arr = ['E','T','M','A'];
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("class_name"))
                .append($("<th>").text("publisher_name"))
                .append($("<th>").text("title"))
                .append($("<th>").text("release_date"))
                .append($("<th>").text("esrb"))
                .append($("<th>").text("actual_price"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.class_name))
                    .append($("<td>").text(record.publisher_name))
                    .append($("<td>").text(record.title))
                    .append($("<td>").text(record.release_date))
                    .append($("<td>").text(record.esrb))
                    .append($("<td>").text(record.actual_price));
            if (publisher_name_arr.includes(record.publisher_name)){
                publisher_name_arr.push(record.publisher_name);
            }
            if (!class_name_arr.includes(record.class_name)){
                class_name_arr.push(record.class_name);
            }
            if (!title_arr.includes(record.title)){
                title_arr.push(record.title);
            }
        });
        key_map = new Map([['class_name',1],['class_name_old',1],['class_name_new',1],
         ['publisher_name',2], ['title',3], ['title_old',3], ['esrb',4]]);
        optionable_map = new Map([[1,class_name_arr],[2, title_arr],[3, class_name_arr],[4,esrb_arr]]);
        selected_table="software";
    });
    openModal();
}

function softwareCreateBuilder(){
    formBuilder(['class_name', 'publisher_name', 'title', 'release_date', 'esrb', 'actual_price'],
    ['class_name', 'publisher_name','esrb']);
}

function softwareUpdateBuilder(){
    formBuilder(['class_name_old', 'title_old', 'class_name_new', 'title_new',
        'publisher_name', 'release_date', 'esrb', 'actual_price'],
        ['class_name_old', 'title_old', 'class_name_new','publisher_name','esrb']);
}

function softwareDeleteBuilder(){
    formBuilder(['class_name', 'title'],['class_name', 'title']);
}

function clickSoftwareClass() {
    $.get("MainServlet",{ tab: 'Software_Class' },  function (responsejson) {
        var $div = $("#db_table");
        $div.empty();
        class_name_arr = [];
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("class_name"))
                .append($("<th>").text("class_parent_name"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.class_name))
                    .append($("<td>").text(record.class_parent_name));
            if (!class_name_arr.includes(record.class_name)){
                class_name_arr.push(record.class_name);
            }
        });
        key_map = new Map([['class_name',1],['class_name_old',1],
            ['class_parent_name',1],['class_parent_name_new',1]]);
        optionable_map = new Map([[1,class_name_arr]]);
        selected_table="software Class";
    });
    openModal();
}

function softwareClassCreateBuilder(){
    formBuilder(['class_name','class_parent_name'],['class_parent_name']);
}

function softwareClassUpdateBuilder(){
    formBuilder(['class_name_old','class_name_new','class_parent_name_new'],
    ['class_name_old','class_parent_name_new']);
}

function softwareClassDeleteBuilder(){
    formBuilder(['class_name'],['class_name']);
}

function formBuilder(keys, selectables) {
    var $ul = $("<ul>").appendTo($div_fields);
    keys.forEach(function (field) {
        var $li = $("<li>").appendTo($ul);
        $("<label>").attr('for', field).text(field + ": ").appendTo($li);
        if (selectables.includes(field)){
               $("<datalist>").attr({id: field+"list"}).appendTo($li);
               $("<input>").attr({type:"text", list: field+"list", name: field}).appendTo($li);
               var options;
               optionable_map.get(key_map.get(field)).forEach(function(email){
                   options += '<option value="'+email+'">';
               });
               $('#'+field+'list').append(options);
            }
            else{
               $("<input>").attr({type: 'text', name: field}).appendTo($li);
        }
    });
}

function onTextChanged(event){
    var $sender = event.target;
    
}

function ProductListSelect() {
    $.get("MainServlet", {tab: 'Product List'}, function (responsejson) {
        var $div = $("#content-tab_list_div");
        $div.empty();
        var $table = $("<table>").appendTo($div);
        $("<tr>").appendTo($table)
                .append($("<th>").text("Title"))
                .append($("<th>").text("Actual Price"));
        $.each(responsejson, function (index, record) {
            $("<tr>").appendTo($table)
                    .append($("<td>").text(record.title))
                    .append($("<td>").text(record.actual_price));
        });
    });
}

