/*******************************************************************************
 * Copyright (c) 2009, 2018 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Elliott Baron <ebaron@redhat.com> - initial API and implementation
 *******************************************************************************/
#ifndef CPPTEST_H_
#define CPPTEST_H_

class Foo {
public:
	Foo() : y(4) {}
	int bar(int);
private:
	int baz(int, int);
	int y;
};

#endif /* CPPTEST_H_ */
