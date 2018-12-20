#!/usr/bin/perl

main();

sub main {

    @tokenType = ( IDENT_OR_KEY, S_LITERAL, I_LITERAL, OP_PLUS, OP_MINUS,
                  OP_MULT, OP_DIV, OP_EQ, L_PAREN, R_PAREN, SEMI, UNDEFINED);
    
    $regEx_key = '^[A-Za-z_][\d_A-Za-z]*$';
    $regEx_slit = '^"(\\"|[^"])*"$';
    $regEx_ilit = '^[\d]+$';
    $regEx_op = '\+';
    $regEx_minus = '-';
    $regEx_mult = '\*';
    $regEx_div = '/';
    $regEx_eq = '=';
    $regEx_lparen = '\(';
    $regEx_rparen = '\)';
    $regEx_semi = ';';
    
    @regEx = ($regEx_key, $regEx_slit, $regEx_ilit, $regEx_op,
                    $regEx_minus, $regEx_mult, $regEx_div, $regEx_eq,
					$regEx_lparen, $regEx_rparen, $regEx_semi);
    
    $filename = 'in3.tinyl';
    $outfile = 'out3.txt';
    
    open (FH1, "< $filename") or die "Cannot open file: $!";
    open (FH2, "> $outfile") or die "Cannot open output file: $!";
    
    @tokenFromFile;
    $tokenSize = @tokenType;
    
    $split = '(?<=\()|(?=\()|(?<=\))|(?=\))|\ |(?<=;)|(?=;)|\R';
        
    while ($line = <FH1>) 
	{
	    chomp $line;
        
	    @linearray = split(/$split/, $line);
	    foreach $token (@linearray)
	    {
			push @tokenFromFile, $token;
	    }
	}
    
    foreach $token (@tokenFromFile)
	{
		$flag = 1;
        
		for ($i=0; $i<$tokenSize; $i++)
		{
			$curr_regex = @regEx[$i];
			
			if ($token =~ /$curr_regex/)
			{
				print FH2 "$token @tokenType[$i]\n";
				$flag = 0;
				last;
			}
		}
        
		if ($flag == 1)
		{
			print FH2 "$token UNDEFINED\n";
		}
	}
     
    close FH1;
    close FH2;
}