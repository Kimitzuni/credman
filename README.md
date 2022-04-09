# credman
*credman* is a cross platform credential manager written in Java

## **BIG NOTE**
I would **NOT** use this for actual storing of passwords, the reason is --- All of it is stored in an unencrypted XML document. It's on the same level as the old `passwords.txt` method of storing your passwords.

If you do want to use a password manager, I would suggest avoid any that you have to pay for or are exclusively online, as one hack will likely render your passwords public information. I would personally recommend either GNU Pass if you like command line utilities, or if you like GUI's I would recommend you take a look at [KeePass XC](https://keepassxc.org/), which is available for Windows, Mac and Linux (Including RHEL)

## Dependencies
As credman is written in Java, you will need to have Java installed on your system

### Installing Java on Linux
Arch:
```sh
# pacman -Sy jre-openjdk jdk-openjdk make
```

### Installing Java on Windows
As Windows doesn't have a true package manager (and no, I'm not counting [WinPKG](https://devblogs.microsoft.com/commandline/windows-package-manager-1-0)), you have
to download a Java Installer, you can download this [here](https://www.java.com/download/ie_manual.jsp)

## Building
To build credman, you can use the Makefile, or run the `javac` command manually. If you decide to use the makefile, you simply just have to use `make`. If you want to run it instead of compiling it to a Java binary, you can use `make run`.

If you want to run the `javac` command manually, you can use this:

```bash
$ javac credman.java
$ java credman
```