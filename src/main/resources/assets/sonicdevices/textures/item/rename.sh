sub="cane"
name1="$sub/Sonic Cane Blue"
name2="$sub/blue"
for i in 1 2 3 4 5; do
	j="0"
	mv "$name1 $i$j.png" "$name2$i$j.png"
	j="1"
	mv "$name1 $i$j.png" "$name2$i$j.png"
done