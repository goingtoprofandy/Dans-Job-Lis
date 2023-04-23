package com.developer.aitek.api

import java.io.IOException

class ApiException(message: String): IOException(message)
class ConnectionException(message: String): IOException(message)