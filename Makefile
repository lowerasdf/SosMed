.PHONY = compile run jar runjar zip all clean

#TODO: edit with path to your javac (java compiler)
JC =  /Library/Java/JavaVirtualMachines/jdk-11.0.4.jdk/Contents/Home/bin/javac

#TODO: edit with path to your java (java runtime environment)
JRE =  /Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib /Library/Java/JavaVirtualMachines/jdk-11.0.4.jdk/Contents/Home/bin/java

#TODO: edit with path to your module-path for javafx
MP = --module-path /Library/Java/JavaVirtualMachines/jdk-11.0.4.jdk/Contents/Home/bin/javac /Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 

#TODO: edit with your classpath from Eclipse 
CP = -classpath /Library/Java/JavaVirtualMachines/jdk-11.0.4.jdk/Contents/Home/bin/java --module-path /Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -classpath /Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/CS400_a:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx-swt.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.base.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.controls.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.fxml.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.graphics.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.media.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.swing.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.web.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/src.zip application.Main

SRC = application/*.java   

APP = application.Main 

ARGS = 

compile:
	$(JC) $(CP) $(SRC) 

run:
	$(JRE) $(MP) $(CP) $(APP) $(ARGS)

jar:
	jar -cvmf manifest.txt executable.jar .

runjar:
	$(JRE) $(MP) -jar executable.jar $(ARGS)

# Create zip file for submitting to handin
zip: 
	zip -r ateam.zip .

#Eclipse's "Show Command Line"
all:
	/Library/Java/JavaVirtualMachines/jdk-11.0.4.jdk/Contents/Home/bin/java --module-path /Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -classpath /Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/CS400_a:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx-swt.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.base.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.controls.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.fxml.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.graphics.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.media.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.swing.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/javafx.web.jar:/Users/bloomestjansenchandra/Desktop/Bloomest\ JC\ Folder/eclipse-workspace/javafx-sdk-11.0.2/lib/src.zip application.Main

# Remove generated files
clean:
	rm -f application/*.class
	rm -f executable.jar


