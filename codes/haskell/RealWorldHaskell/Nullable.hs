-- file: ch03/Nullable.hs

-- a是类型变量
data MyMaybe a = MyJust a
               | MyNothing
                 deriving (Show)

someBool = MyJust True
someString = MyJust "something"
