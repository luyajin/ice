// **********************************************************************
//
// Copyright (c) 2003-2018 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

using Test;

namespace Ice
{
    namespace scope
    {
        public class Client : TestHelper
        {
            public override void run(string[] args)
            {
                var properties = createTestProperties(ref args);
                properties.setProperty("Ice.Package.Test", "Ice.scope");
                using(var communicator = initialize(properties))
                {
                    var output = getWriter();
                    output.Write("test same Slice type name in different scopes... ");
                    output.Flush();
                    AllTests.allTests(this);
                    output.WriteLine("ok");
                }
            }

            public static int Main(string[] args)
            {
                return TestDriver.runTest<Client>(args);
            }
        }
    }
}
