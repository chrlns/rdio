/*
 *  Bordfunk Internet Radio App
 *  Copyright (C) 2010-2011 Christian Lins <christian@lins.me>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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
