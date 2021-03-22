-- file: ch01/WC.hs
-- runghc WC < ../data/quux.txt

main = interact wordCount
  where wordCount input = show (length (lines input)) ++ "\n"
