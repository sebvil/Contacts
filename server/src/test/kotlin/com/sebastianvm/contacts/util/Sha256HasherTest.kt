package com.sebastianvm.contacts.util

import com.sebastianvm.contacts.testutils.baseTestSuite
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

val Sha256HasherTest by baseTestSuite {
    testSuite("hash") {
        test("returns a different value than the input") {
            tokenHasher.hash("my-token") shouldNotBe "my-token"
        }

        test("is deterministic for the same input") {
            tokenHasher.hash("my-token") shouldBe tokenHasher.hash("my-token")
        }
    }

    testSuite("verify") {
        test("returns true for matching input and hash") {
            val hash = tokenHasher.hash("my-token")
            tokenHasher.verify("my-token", hash) shouldBe true
        }

        test("returns false for different input") {
            val hash = tokenHasher.hash("my-token")
            tokenHasher.verify("different-token", hash) shouldBe false
        }
    }
}
