# Gimbal
Assists with orientation locking and sensor event normalization

There are many apps that lock orientation. There are good and bad reasons for this. But many times, locking a single activity within your app would suffice. Gimbal serves that purpose. When tied to an activity, it gives you control over locking and unlocking the current orientation of the user. This way, if the user prefers to use your app in landscape, they will not have to be forced into portrait in a single activty, but instead are locked into their current orientation. 

[![Build Status](https://travis-ci.org/Commit451/Gimbal.svg?branch=master)](https://travis-ci.org/Commit451/Gimbal)
[![](https://jitpack.io/v/Commit451/Gimbal.svg)](https://jitpack.io/#Commit451/Gimbal)

# Usage
If you are using this library to lock orientation on certain activities, usage is simple:
```java
//this == activity
Gimbal gimbal = new Gimbal(this);
gimbal.lock();
//later, if need be
gimbal.unlock();
```
See the sample project for a comprehensive example. 

License
--------

    Copyright 2016 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
