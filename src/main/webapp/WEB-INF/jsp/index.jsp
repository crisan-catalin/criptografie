<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<jsp:include page="header.jsp"></jsp:include>

<form>
    <div class="d-flex justify-content-center">
        <div class="col-sm-12 col-md-4 col-lg-3 d-flex flex-column justify-content-around">
            <div class="form-group">
                <label for="cipher">Encrypting algorithm</label>
                <select id="cipher" name="cipher" class="form-control" required>
                    <option value="" selected disabled>Select a cipher</option>
                    <c:forEach var="cipher" items="${CIPHERS_LIST}">
                        <option value="${cipher.id}">${cipher.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="encryptedFile">Load an encrypted file</label>
                <input type="file" class="form-control-file" id="encryptedFile">
            </div>
        </div>

        <div class="col-sm-12 col-md-4 col-lg-3 d-flex flex-column justify-content-around">
            <button type="submit" class="btn btn-block btn-success">Encrypt</button>
            <button type="submit" class="btn btn-block btn-warning">Decrypt</button>
        </div>
    </div>

    <div class="d-flex justify-content-center">
        <div class="col-sm-12 col-md-4 col-lg-3 d-flex flex-column justify-content-around">
            <div class="form-group">
                <label for="keyA">Key (A)</label>
                <input type="email" class="form-control" id="keyA" aria-describedby="errorKeyA">
                <small id="errorKeyA" class="form-text text-danger invisible">Invalid key.</small>
            </div>
        </div>
        <div class="col-sm-12 col-md-4 col-lg-3 d-flex flex-column justify-content-around">
            <div class="form-group">
                <label for="keyB">Key (B)</label>
                <input type="email" class="form-control" id="keyB" aria-describedby="errorKeyB">
                <small id="errorKeyB" class="form-text text-danger invisible">Invalid key.</small>
            </div>
        </div>
    </div>
</form>

<div class="d-flex justify-content-center">
    <div class="col-sm-12 col-md-6 col-lg-4">
        <div class="form-group">
            <label for="decryptedText">Decrypted text</label>
            <textarea id="decryptedText" class="form-control" name="decrypted" rows="10" cols="70"></textarea>
        </div>
    </div>
    <div class="col-sm-12 col-md-6 col-lg-4">
        <div class="form-group">
            <label for="encryptedText">Encrypted text</label>
            <textarea id="encryptedText" class="form-control" name="encrypted" rows="10" cols="70"></textarea>
        </div>
    </div>
</div>

<div class="flex-grow-1"></div>

<jsp:include page="footer.jsp"></jsp:include>