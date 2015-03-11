// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

package IceInternal;

public final class LocatorManager
{
    LocatorManager(Ice.Properties properties)
    {
        _background = properties.getPropertyAsInt("Ice.BackgroundLocatorCacheUpdates") > 0;
    }

    synchronized void
    destroy()
    {
        for(LocatorInfo info : _table.values())
        {
            info.destroy();
        }
        _table.clear();
        _locatorTables.clear();
    }

    //
    // Returns locator info for a given locator. Automatically creates
    // the locator info if it doesn't exist yet.
    //
    public LocatorInfo
    get(Ice.LocatorPrx loc)
    {
        if(loc == null)
        {
            return null;
        }

        //
        // The locator can't be located.
        //
        Ice.LocatorPrx locator = Ice.LocatorPrxHelper.uncheckedCast(loc.ice_locator(null));

        //
        // TODO: reap unused locator info objects?
        //

        synchronized(this)
        {
            LocatorInfo info = _table.get(locator);
            if(info == null)
            {
                //
                // Rely on locator identity for the adapter table. We want to
                // have only one table per locator (not one per locator
                // proxy).
                //
                LocatorTable table = _locatorTables.get(locator.ice_getIdentity());
                if(table == null)
                {
                    table = new LocatorTable();
                    _locatorTables.put(locator.ice_getIdentity(), table);
                }

                info = new LocatorInfo(locator, table, _background);
                _table.put(locator, info);
            }

            return info;
        }
    }

    final private boolean _background;

    private java.util.HashMap<Ice.LocatorPrx, LocatorInfo> _table =
        new java.util.HashMap<Ice.LocatorPrx, LocatorInfo>();
    private java.util.HashMap<Ice.Identity, LocatorTable> _locatorTables =
        new java.util.HashMap<Ice.Identity, LocatorTable>();
}