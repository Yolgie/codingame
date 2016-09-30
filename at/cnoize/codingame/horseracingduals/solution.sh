#!/usr/bin/env bash

read N
for (( i=0; i<N; i++ )); do
    read UnsortedArray[i]
done

# echo "Unsorted: ${UnsortedArray[@]}">&2

readarray -t SortedArray < <(printf '%s\n' "${UnsortedArray[@]}" | sort -g)

# echo "Sorted: ${SortedArray[@]}">&2

prev=${SortedArray[0]}
diff=$((${SortedArray[1]}-${SortedArray[0]}))
for (( i=0; i<N-1; i++ )); do
    next=${SortedArray[i+1]}
    ndiff=$(($next-$prev))
    if ((ndiff<diff));
        then diff=${ndiff}
    fi
    prev=$next
done

echo "${diff}"