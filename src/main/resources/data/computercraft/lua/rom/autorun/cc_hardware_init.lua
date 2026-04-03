if _ccHardware then
    local _isAdvanced = _ccHardware.advanced()
    local _nativeIsColour = term.isColour
    term.isColour = function()
        if not _isAdvanced then return false end
        return _nativeIsColour()
    end
    term.isColor = term.isColour
end