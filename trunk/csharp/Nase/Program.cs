using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Nase.Syntax;

namespace Nase
{
    class Program
    {
        static readonly Logger Logger = LogManager.CreateLogger("NASE");

        static void Main(string[] args)
        {
            // Watch for unhandled exceptions
            AppDomain.CurrentDomain.UnhandledException += UnhandledExceptionHandler;

            LogManager.Enabled = true;
            LogManager.AttachLogTarget(new ConsoleTarget(Level.Trace));

            Console.ForegroundColor = ConsoleColor.Yellow;
            PrintBanner();
            Console.ResetColor();

            if (args.Length < 1 ||
                Path.GetExtension(args[0]) != FileManager.INPUT_FILE_EXTENSION)
            {
                PrintUsage();
                Environment.Exit(1);
            }

            using (FileManager fileManager = new FileManager(args[0]))
            {
                SyntaxTree syntaxTree;
                if (RunPhase_1(fileManager, out syntaxTree) &&
                    RunPhase_2() &&
                    RunPhase_3())
                {
                    Logger.Info("Compilation successful.");
                }
                fileManager.FinalizeListing();
            }
        }

        static bool RunPhase_1(FileManager fileManager, out SyntaxTree syntaxTree)
        {
            Scanner scanner = new Scanner(fileManager);
            Parser parser = new Parser(fileManager, scanner);

            return parser.ParseProgramm(out syntaxTree);
        }

        static bool RunPhase_2()
        {
            return true;
        }

        static bool RunPhase_3()
        {
            return true;
        }

        static void PrintBanner()
        {
            Console.WriteLine();
            Console.WriteLine(@"    __                         ");
            Console.WriteLine(@"   / _|                        ");
            Console.WriteLine(@"  | '                          ");
            Console.WriteLine(@" _| |_   _    _                ");
            Console.WriteLine(@"|_   _| | |  | |               ");
            Console.WriteLine(@"  | |   | |__| |               ");
            Console.WriteLine(@"  | |   |  __  |               ");
            Console.WriteLine(@"  | |   | |  | |               ");
            Console.WriteLine(@"  |_|   |_|  |_| Kaiserslautern");

            Console.WriteLine();
            Console.WriteLine(@"Compiler NASE");
            Console.WriteLine(@"This program has been developed for education purposes only.");
            Console.WriteLine(@"Use it at your own risk.");
            Console.WriteLine();
        }

        static void PrintUsage()
        {
            Console.WriteLine();
            Console.WriteLine(@"Usage {0} <FILENAME>", System.IO.Path.GetFileName(System.Diagnostics.Process.GetCurrentProcess().MainModule.FileName));
            Console.WriteLine(@"   FILENAME : input file ({0})", FileManager.INPUT_FILE_EXTENSION);
            Console.WriteLine();
        }

        static void UnhandledExceptionHandler(object sender, UnhandledExceptionEventArgs e)
        {
            if (e.IsTerminating)
                Logger.FatalException((e.ExceptionObject as Exception), "Application terminating because of unhandled exception.");
            else
                Logger.ErrorException((e.ExceptionObject as Exception), "Caught unhandled exception.");
        }
    }
}
