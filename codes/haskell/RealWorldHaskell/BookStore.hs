-- file: ch03/BookStore.hs

-- Prelude> :type last
-- last :: [a] -> a
-- Prelude> :load BookStore
-- [1 of 1] Compiling Main             ( BookStore.hs, interpreted )
-- Ok, one module loaded.
-- *Main> myInfo
-- Book 9780135072455 "Algebra of Programming" ["Richard Bird","Oege de Moor"]
-- *Main> :type myInfo
-- myInfo :: BookInfo
-- *Main> :type BookInfo
--
-- <interactive>:1:1: error: Data constructor not in scope: BookInfo
-- *Main> :type Book
-- Book :: Int -> String -> [String] -> BookInfo
-- *Main> :info BookInfo
-- data BookInfo = Book Int String [String]
--   	-- Defined at BookStore.hs:3:1
-- instance [safe] Show BookInfo -- Defined at BookStore.hs:4:27

data BookInfo = Book Int String [String]
                deriving (Show)

data MagazineInfo = Magazine Int String [String]
                    deriving (Show)

-- 类型别名
type CustomerID = Int
type ReviewBody = String
data BookReview = BookReview BookInfo CustomerID ReviewBody

type BookRecord = (BookInfo, BookReview)

myInfo = Book 9780135072455 "Algebra of Programming"
         ["Richard Bird", "Oege de Moor"]


-- 代数数据类型
type CardNumber = String
type CardHolder = String
type Address = [String]
data BillingInfo = CreaditCard CardNumber CardHolder Address
                 | CashOnDelivery
                 | Invoice CustomerID
                   deriving (Show)

-- 使用模式匹配访问slot
-- *Main> boolID myInfo
-- 9780135072455
-- *Main> boolTitle myInfo
-- "Algebra of Programming"
-- *Main> boolAuthors myInfo
-- ["Richard Bird","Oege de Moor"]
boolID (Book id title authors) = id
boolTitle (Book id title authors) = title
boolAuthors (Book id title authors) = authors


-- 记录语法
data Customer = Customer {
  customerID :: CustomerID,
  customerName :: String,
  customerAddress :: Address
} deriving (Show)

customer1 = Customer 281828 "J.R. Hacker"
            ["255 Syntax Ct", "Milpitas, CA 95134", "USA"]
customer2 = Customer {
  customerID = 271828,
  customerAddress = ["1048576 Disk Drive", "Milpitas, CA 95134", "USA"],
  customerName = "Jane Q. Citizen"
}
