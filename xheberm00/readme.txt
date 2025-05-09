IJA projekt - hra LightBulb

autoři:	Matyáš Hebert	xheberm00
	Jan Ostatnický	xostatj00

překlad a spuštění:
			1)za pomocí Makefile:	make		-- přeloží, vytvoří programovou dokumentaci a spustí
						make clean	-- odstraní adresář target a všechny artefakty pozůstalé z překladu
						make install	-- nainstaluje artefakty do lokálního maven repositáře
						make build	-- přeloží aplikaci
						make package	-- vytvoří jar archiv
						make javadoc	-- vytvoří programovou dokumentaci, otevřít v prohlížeči přes soubor target/reports/apidocs/index.html
						make run	-- spustí aplikaci

			2)přiloženým skriptem:	./compile_project	-- skript musí mít právo spuštění

			3) manuálně:	mvn install
					mvn compile
					mvn package
					mvn javafx:run | java -jar target/game-project-1.0-SNAPSHOT-jar-with-dependencies.jar // ke druhé možnosti je nutné přiložit javafx jako moduly pomocí --module-path a --add-modules
