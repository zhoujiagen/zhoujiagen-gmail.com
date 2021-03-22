-- file: ch03/ListADT.hs

data List a = Cons a (List a)
            | Nil
              deriving (Show)

list = Cons 3 (Cons 2 (Cons 1 (Cons 0 Nil)))
