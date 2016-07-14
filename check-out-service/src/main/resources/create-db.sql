CREATE TABLE productBill (
  billId   BIGINT PRIMARY KEY,
  amountBase DECIMAL,
  taxRate DECIMAL,
  tax DECIMAL,
  amountTotal DECIMAL,
  productId BIGINT
);