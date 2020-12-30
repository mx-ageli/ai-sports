/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';

    config.uiColor = '#FAFAFA';

    config.filebrowserUploadUrl="/api/basics/v/upload_by_editor?type=image";

    config.image_previewText=' ';
};
