# Problem Set 4A

def get_permutations(sequence):
    '''
    Enumerate all permutations of a given string

    sequence (string): an arbitrary string to permute. Assume that it is a
    non-empty string.  

    You MUST use recursion for this part. Non-recursive solutions will not be
    accepted.

    Returns: a list of all permutations of sequence

    Example:
    >>> get_permutations('abc')
    ['abc', 'acb', 'bac', 'bca', 'cab', 'cba']

    Note: depending on your implementation, you may return the permutations in
    a different order than what is listed here.
    '''
    
    if len(sequence) == 1:
        return list(sequence)

    perms = get_permutations(sequence[1:])

    ans = list()
    for i, perm in enumerate(perms):
        for j in range(len(perm)+1):
            ans.append(perm[:j] + sequence[0] + perm[j:])

    return ans

if __name__ == '__main__':
#    #EXAMPLE
#    example_input = 'abc'
#    print('Input:', example_input)
#    print('Expected Output:', ['abc', 'acb', 'bac', 'bca', 'cab', 'cba'])
#    print('Actual Output:', get_permutations(example_input))
    
#    # Put three example test cases here (for your sanity, limit your inputs
#    to be three characters or fewer as you will have n! permutations for a 
#    sequence of length n)

    #EXAMPLE 1
    example_input_1 = 'dfg'
    print('Input:', example_input_1)
    print('Expected Output:', ['dfg', 'fdg', 'fgd', 'dgf', 'gdf', 'gfd'])
    print('Actual Output:', get_permutations(example_input_1))

    #EXAMPLE 2
    example_input_2 = '1234'
    print('Input:', example_input_2)
    print('Expected Output:', ['1234', '2134', '2314', '2341', '1324', '3124', 
    '3214', '3241', '1342', '3142', '3412', '3421', '1243', '2143', '2413',
    '2431', '1423', '4123', '4213', '4231', '1432', '4132', '4312', '4321']) # length == 24
    print('Actual Output:', get_permutations(example_input_2))

    #EXAMPLE 3
    example_input_3 = '+_O'
    print('Input:', example_input_3)
    print('Expected Output:', ['+_O', '_+O', '_O+', '+O_', 'O+_', 'O_+'])
    print('Actual Output:', get_permutations(example_input_3))