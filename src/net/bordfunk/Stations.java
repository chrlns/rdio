/*
 *   Bordfunk - Internet Radio App
 *   Copyright (C) 2010-2011 Christian Lins <christian@lins.me>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.bordfunk;

/**
 *
 * @author Christian Lins
 */
public class Stations {

	public static final String[] DESCRIPTIONS = new String[]
	{
		"Das Jugendprogramm des Westdeutschen Rundfunks.",
		"Deutschlandfunk",
		"Deutschlandradio Kultur",
		"DRadio Wissen ist das dritte Vollprogramm des Deutschlandradios.",
		"NDR 2 - Das Beste am Norden"
	};

	public static final String[] NAMES = new String[]
	{
		"1LIVE",
		"Deutschlandfunk",
		"DRadio Kultur",
		"DRadio Wissen",
		"NDR 2"
	};

	public static final String[] HOSTS = new String[]
	{
		"1live.akacast.akamaistream.net",
		"dradio.ic.llnwd.net",
		"dradio.ic.llnwd.net",
		"dradio.ic.llnwd.net",
		"ndrstream.ic.llnwd.net"
	};

	public static final String[] URLS = new String[]
	{
		"/7/706/119434/v1/gnl.akacast.akamaistream.net/1live",
		"/stream/dradio_dlf_s_a",
		"/stream/dradio_dkultur_s_a",
		"/stream/dradio_dwissen_s_a",
		"/stream/ndrstream_ndr2_hi_mp3",
	};
}
