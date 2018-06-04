BEGIN TRANSACTION;
CREATE TABLE `Transport_Master_Table` (
	`name`	TEXT NOT NULL,
	PRIMARY KEY(`name`)
);
CREATE TABLE `Supplier_Master_Table` (
	`Name`	TEXT NOT NULL,
	PRIMARY KEY(`Name`)
);
CREATE TABLE "Payment_Entry_Table" (
	`Voucher No.`	TEXT NOT NULL,
	`Voucher Date`	TEXT NOT NULL,
	`Supplier Name`	TEXT NOT NULL,
	`Total Amount`	TEXT NOT NULL,
	PRIMARY KEY(`Voucher No.`)
);
CREATE TABLE "Payment_Entry_Extended_Table" (
	`Voucher No.`	TEXT NOT NULL,
	`Buyer Name`	TEXT NOT NULL,
	`Bill No.`	TEXT NOT NULL,
	`Bill Date`	TEXT NOT NULL,
	`Bill Amount`	TEXT NOT NULL,
	`GR`	TEXT NOT NULL,
	`RD`	TEXT NOT NULL,
	`CD`	TEXT NOT NULL,
	`BC`	TEXT NOT NULL,
	`Due Amount`	TEXT NOT NULL,
	`Amount Paid`	TEXT NOT NULL,
	`Bank`	TEXT NOT NULL,
	`DD No.`	TEXT NOT NULL,
	`DD Date`	TEXT NOT NULL
);
CREATE TABLE `LR_Table` (
	`Bill No.`	TEXT NOT NULL,
	`LR No.`	TEXT NOT NULL,
	`PM`	TEXT NOT NULL
);
CREATE TABLE "Collection_Entry_Table" (
	`Voucher No.`	TEXT NOT NULL,
	`Voucher Date`	TEXT NOT NULL,
	`Buyer Name`	TEXT NOT NULL,
	`Total Amount`	TEXT NOT NULL,
	PRIMARY KEY(`Voucher No.`)
);
CREATE TABLE "Collection_Entry_Extended_Table" (
	`Voucher No.`	TEXT NOT NULL,
	`Supplier Name`	TEXT NOT NULL,
	`Bill No.`	TEXT NOT NULL,
	`Bill Date`	TEXT NOT NULL,
	`Bill Amount`	TEXT NOT NULL,
	`GR`	TEXT NOT NULL,
	`RD`	TEXT NOT NULL,
	`CD`	TEXT NOT NULL,
	`BC`	TEXT NOT NULL,
	`Collection Due`	TEXT NOT NULL,
	`Amount Collected`	TEXT NOT NULL,
	`Bank`	TEXT NOT NULL,
	`DD No.`	TEXT NOT NULL,
	`DD Date`	TEXT NOT NULL
);
CREATE TABLE `Buyer_Master_Table` (
	`Name`	TEXT NOT NULL,
	PRIMARY KEY(`Name`)
);
CREATE TABLE "Bill_Entry_Table" (
	`Supplier Name`	TEXT NOT NULL,
	`Buyer Name`	TEXT NOT NULL,
	`Bill No.`	TEXT NOT NULL,
	`Bill Date`	TEXT NOT NULL,
	`Transport`	TEXT NOT NULL,
	`LR Date`	TEXT NOT NULL,
	`Bill Amount`	TEXT NOT NULL,
	`Collection Due`	TEXT NOT NULL,
	`Due`	TEXT NOT NULL,
	PRIMARY KEY(`Bill No.`)
);
COMMIT;
