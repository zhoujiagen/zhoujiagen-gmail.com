-- file: ch02/add.hs
-- Prelude> :load add.hs
-- [1 of 1] Compiling Main             ( add.hs, interpreted )
-- Ok, one module loaded.
-- *Main> add 1 2
-- 3
--
-- *Main> :show modules
-- Main             ( add.hs, interpreted )
-- *Main> :unadd add.hs
-- Ok, no modules loaded.
-- Prelude>

add a b = a + b

myNot True = False
myNot False = True

sumList (x:xs) = x + sumList xs
sumList [] = 0
